package fun.hercules.order.order.platform.security;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class AcgListByIdsRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        if (!request.getRequestURI().matches("/acg/orders.*")) {
            return false;
        }
        return !ArrayUtils.isEmpty(request.getParameterValues("ids"));
    }
}
