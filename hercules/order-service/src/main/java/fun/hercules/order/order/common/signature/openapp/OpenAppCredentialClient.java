package fun.hercules.order.order.common.signature.openapp;

import fun.hercules.order.order.common.signature.annotations.JwtSignature;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@JwtSignature
@FeignClient(name = "open-app-credential", url = "${hlp.endpoints.user}")
public interface OpenAppCredentialClient {


    @RequestMapping(method = RequestMethod.GET, path = "/open-apps")
    List<AppCredential> get(@RequestParam("name") String appName);
}
