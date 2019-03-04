package fun.hercules.webapi.security;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class AcgListByIdsRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        if (!request.getRequestURI().matches("/webapi/acg/orders.*")) {
            return false;
        }
        if (ArrayUtils.isEmpty(request.getParameterValues("ids"))) {
            return false;
        }
        request.setAttribute("captcha", String.valueOf(request.getParameter("captcha")));
        request.setAttribute("captchaId", String.valueOf(request.getParameter("captchaId")));
        return true;
    }
}
