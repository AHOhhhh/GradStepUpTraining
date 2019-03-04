package fun.hercules.webapi.filters;

import fun.hercules.webapi.client.UserClient;
import fun.hercules.webapi.security.ErrorCode;
import fun.hercules.webapi.security.Pair;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UncheckedIOException;

@Component
public class CaptchaVerfierFilter extends ZuulFilter {

    private final boolean captchaEnabled;

    private final UserClient userClient;

    public CaptchaVerfierFilter(@Value("${verify.captcha}") boolean captchaEnabled,
                                UserClient userClient) {
        this.captchaEnabled = captchaEnabled;
        this.userClient = userClient;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        if (!captchaEnabled) {
            return false;
        }
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        return request.getAttribute("captcha") != null;

    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (!userClient.verifyCaptcha(new Pair(String.valueOf(request.getAttribute("captchaId")),
                String.valueOf(request.getAttribute("captcha"))))) {
            try {
                ctx.getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED, ErrorCode.INVALID_CAPTCHA.getMessage());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        return null;
    }
}
