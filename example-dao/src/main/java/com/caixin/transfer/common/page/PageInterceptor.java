package com.caixin.transfer.common.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * mybatis分页公共模块 分页拦截器
 * 
 * @author linxz
 * @date 2014年12月17日 上午9:24:35
 * @Description:
 */
@Intercepts({ // 只拦截查询部分
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }) })
public class PageInterceptor implements Interceptor {
    private Dialect dialect = new MysqlDialect();

    /** 静态内部类 */
    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    public Object plugin(Object obj) {
        return Plugin.wrap(obj, this);
    }

    public void setProperties(Properties prop) {

    }

    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Configuration cfg = mappedStatement.getConfiguration();
        Object parameter = invocation.getArgs()[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        if (boundSql == null || StringUtils.isBlank(boundSql.getSql())) {
            return null;
        }
        String originalSql = boundSql.getSql().trim();
        RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
        Object parameterObject = boundSql.getParameterObject();

        // 分页参数
        Page page = null;

        // page入口为map传参形式
        if (parameterObject != null) {
            Object obj = this.isPage(parameterObject, "page");

            if (null != obj) {
                page = (Page) obj;
            }
        }

        if (page != null) {
            int totpage = page.getTotalRows();

            // 如果总记录数为零，得到总记录数
            if (totpage == 0) {
                StringBuffer cSql = new StringBuffer(originalSql.length() + 100);

                cSql.append("select count(1) from (").append(originalSql).append(") t");

                String countSql = cSql.toString();
                Connection connection = cfg.getEnvironment().getDataSource().getConnection();
                PreparedStatement countStmt = connection.prepareStatement(countSql.toString());

                BoundSql countBS = copyFromBoundSql(cfg, countSql, boundSql);
                setParameters(countStmt, mappedStatement, countBS, parameterObject);
                ResultSet rs = countStmt.executeQuery();

                if (rs.next()) {
                    totpage = rs.getInt(1);
                }

                rs.close();
                countStmt.close();
                connection.close();
            }

            // 分页计算
            page.init(totpage, page.getPageSize(), page.getCurrentPage());

            if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
                rowBounds = new RowBounds(page.getPageSize() * (page.getCurrentPage() - 1), page.getPageSize());
            }

            // 分页查询 本地化对象 修改数据库注意修改实现
            int offset = rowBounds.getOffset(), limit = rowBounds.getLimit();
            String pagesql = dialect.getLimitString(originalSql, offset, limit);
            int noOff = RowBounds.NO_ROW_OFFSET, noLi = RowBounds.NO_ROW_LIMIT;
            invocation.getArgs()[2] = new RowBounds(noOff, noLi);

            BoundSql newBoundSql = copyFromBoundSql(cfg, pagesql, boundSql);
            SqlSource ss = new BoundSqlSqlSource(newBoundSql);
            MappedStatement newMs = copyFromMappedStatement(mappedStatement, ss);
            invocation.getArgs()[0] = newMs;
        }

        return invocation.proceed();
    }

    /**
     * 复制BoundSql对象
     */
    private BoundSql copyFromBoundSql(Configuration cfg, String sql, BoundSql boundSql) {
        BoundSql newBoundSql = new BoundSql(cfg, sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    /**
     * 检查是否含有分页或本来就是分页类
     * 
     * @param obj
     * @param fieldName
     * @return Object
     */
    public Object isPage(Object obj, String fieldName) {
        if (obj instanceof Map<?, ?>) {// 如果是Map映射
            try {
                Map<?, ?> map = (Map<?, ?>) obj;
                return map.get(fieldName);
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 对SQL参数(?)设值
     * 参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
     * 
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
                ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        MappedStatement newMs = builder.build();
        return newMs;
    }

}
