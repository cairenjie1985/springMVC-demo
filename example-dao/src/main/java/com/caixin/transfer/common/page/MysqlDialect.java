package com.caixin.transfer.common.page;

/**
 * mybatis分页公共模块
 * mysql方言
 * @author linxz 
 * @date 2014年12月17日 上午10:24:38 
 * @Description:
 */
public class MysqlDialect extends Dialect {

	public String getLimitString(String sql, int offset, int limit) {
		StringBuffer pagingSelect = new StringBuffer(sql);
		pagingSelect.append(" limit ").append(offset).append(",").append(limit);
		return pagingSelect.toString();
	}

}
