package com.simonschuster.pimsleur.unlimited.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestThreadLocalUtils {

    public static String getParameter(String key) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        if (httpServletRequest != null) {
            return httpServletRequest.getParameter(key);
        }
        return null;
    }


    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getRequest();
        }
        return null;
    }
}
