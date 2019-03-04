package fun.hercules.user.utils.captcha;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * 登录验证码生成图片
 */
@Service
public class CaptchaService {

    private ImageCaptchaService imageCaptchaService;

    @Autowired
    public CaptchaService(RedisCaptchaStore redisCaptchaStore) {
        imageCaptchaService = new DefaultManageableImageCaptchaService(
                redisCaptchaStore,
                new CaptchaImageEngine(),
                180,
                100000,
                75000
        );
    }

    public BufferedImage getImageChallengeForID(String captchaId) {
        return imageCaptchaService.getImageChallengeForID(captchaId);
    }

    public boolean isValidResponse(String captchaId, String response) {
        try {
            return imageCaptchaService.validateResponseForID(captchaId, response);
        } catch (CaptchaServiceException e) {
            return false;
        }
    }
}
