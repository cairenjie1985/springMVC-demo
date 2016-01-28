package com.caixin.transfer.controller.common.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.caixin.transfer.util.StringTools;

public class SessionFilter implements HandlerInterceptor {

    public String[]             allowUrls;     // 还没发现可以直接配置不拦截的资源，所以在代码里面来排除
    private final static String HTM  = ".htm";
    private final static String JSON = ".json";
    private final static String ERR  = ".err";
    private final static String JPEG = ".jpeg";

    public String[] getAllowUrls() {
        return allowUrls;
    }

    public void setAllowUrls(String[] allowUrls) {
        this.allowUrls = allowUrls;
    }

    public void destroy() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String uri = request.getServletPath();
        if (uri.indexOf(HTM) > 0 || uri.indexOf(JSON) > 0 || uri.indexOf(ERR) > 0 || uri.indexOf(JPEG) > 0) {

            if (null != allowUrls && allowUrls.length >= 1) {
                for (String url : allowUrls) {
                    if (uri.contains(url)) {
                        return true;
                    }
                }
            }

            doFilter(request, uri, response);
            return true;
        } else {
            return true;
        }

    }

    private boolean doFilter(HttpServletRequest request, String uri, HttpServletResponse response) throws IOException {
        String queryString = request.getQueryString();
        String redirectUrl = "";
        if (!StringTools.isNullOrSpace(queryString) && uri.indexOf(HTM) > 0) {
            redirectUrl = uri;
            redirectUrl = redirectUrl + "?" + request.getQueryString();
        }
        /*  
        BussMembersInfo memberInfo = (BussMembersInfo) request.getSession()
                .getAttribute(BeanConstants.SESSION_CUSTOMER_USER);
        request.getSession().setAttribute(FinalDatas.REDIRECTURL, redirectUrl);
        if (memberInfo != null) {
            return true;
        } else {
            LogUtil.info("session失效或没有登录而操作此链接，抛出异常后进行跳转");
            response.sendRedirect("../login/login.htm?redirect=" + redirectUrl);
            return false;
        }
        */
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        
    }
}
