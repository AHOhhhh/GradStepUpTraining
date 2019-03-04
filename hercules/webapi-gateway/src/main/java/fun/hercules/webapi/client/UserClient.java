package fun.hercules.webapi.client;

import fun.hercules.webapi.security.Pair;
import fun.hercules.webapi.security.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("user-service")
public interface UserClient {

    @RequestMapping(method = RequestMethod.POST, path = "/verify-password")
    User verifyPassword(@RequestBody Pair loginInfo);


    @RequestMapping(method = RequestMethod.POST, path = "/verify-captcha")
    boolean verifyCaptcha(@RequestBody Pair captcha);

    @RequestMapping(method = RequestMethod.GET, path = "/users/{id}")
    User getUserById(@PathVariable("id") String id);

}

