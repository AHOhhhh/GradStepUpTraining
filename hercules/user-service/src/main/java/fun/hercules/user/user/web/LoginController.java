package fun.hercules.user.user.web;

import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import fun.hercules.user.utils.Pair;
import fun.hercules.user.utils.captcha.CaptchaService;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class LoginController {

    private final CaptchaService captchaService;

    private final UserService userService;

    public LoginController(CaptchaService captchaService, UserService userService) {
        this.captchaService = captchaService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    @ApiOperation(value = "login")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = String.class, message = "Login successfully"),
            @ApiResponse(code = 401, message = "Unauthorized ")
    })
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("");
    }


    @RequestMapping(method = RequestMethod.GET, path = "/captcha")
    @ApiOperation(value = "get captcha image")
    public void captcha(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
        try {
            BufferedImage captchaImage = captchaService.getImageChallengeForID(request.getParameter("captchaId"));

            response.setContentType("image/jpeg");
            ImageIO.write(captchaImage, "jpg", response.getOutputStream());
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/verify-captcha")
    @ApiOperation(value = "verify captcha")
    public boolean verifyCaptcha(@RequestBody Pair captcha) {
        return captchaService.isValidResponse(captcha.getKey(), captcha.getValue().toUpperCase());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/verify-password")
    public User verifyPassword(@RequestBody Pair loginInfo) {
        return userService.verifyPassword(loginInfo);
    }
}
