package com.simonschuster.pimsleur.unlimited.utils;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * If create a new thread to execute you code, the class should't be used
 */
public class UnlimitedThreadLocalUtils {


    public static final String REQUEST_ID = "RequestId";
    /**
     * record the extraInfo that will need in some other place;
     * if you use it, you must call remove() finally
     */
    private static ThreadLocal<Map<String, String>> extraInfoThreadLocal = new ThreadLocal<>();

    public static void putExtraInfo(String key, String value) {
        Map<String, String> extraInfo = extraInfoThreadLocal.get();
        if (extraInfo == null) {
            extraInfo = new HashMap<>();
            extraInfoThreadLocal.set(extraInfo);
        }
        extraInfo.put(key, value);
    }

    public static String getExtraInfo(String key) {
        Map<String, String> info = extraInfoThreadLocal.get();
        if (info != null) {
            info.get(key);
        }
        return null;
    }

    public static void remove() {
        extraInfoThreadLocal.remove();
    }

    public static String getRequestParameter(String key) {
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
