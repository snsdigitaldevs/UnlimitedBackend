package com.simonschuster.pimsleur.unlimited.filter;

import com.simonschuster.pimsleur.unlimited.utils.UnlimitedThreadLocalUtils;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

public class RequestLogFilter extends AbstractRequestLoggingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLogFilter.class);


    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        UnlimitedThreadLocalUtils
            .putExtraInfo(UnlimitedThreadLocalUtils.REQUEST_ID, String.valueOf(UUID.randomUUID()));
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        if (!message.contains("password")) {
            LOG.info("RequestId:{}, {}",
                UnlimitedThreadLocalUtils.getExtraInfo(UnlimitedThreadLocalUtils.REQUEST_ID),
                message);
        }
        UnlimitedThreadLocalUtils.remove();
    }
}
