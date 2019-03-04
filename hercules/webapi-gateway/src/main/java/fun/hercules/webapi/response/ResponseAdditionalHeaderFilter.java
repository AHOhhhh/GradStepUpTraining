package fun.hercules.webapi.response;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Component
public class ResponseAdditionalHeaderFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");

//        response.addHeader("Cache-Control", "no-cache, must-revalidate");
//        response.addHeader("Pragma", "no-cache");

        String contentTypeOri = response.getContentType();
        if (contentTypeOri != null && !contentTypeOri.toLowerCase().contains("charset")) {
            response.addHeader("Content-Type", contentTypeOri + ";charset=utf-8");
        }

        return null;
    }
}
