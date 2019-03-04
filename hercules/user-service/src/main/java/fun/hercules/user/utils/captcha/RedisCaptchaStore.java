package fun.hercules.user.utils.captcha;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.CaptchaAndLocale;
import com.octo.captcha.service.captchastore.CaptchaStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录验证码保存在redis中供校验
 */
@Component
public class RedisCaptchaStore implements CaptchaStore {

    private Map<String, Serializable> valueOps;


    public RedisCaptchaStore() {
        this.valueOps = new ConcurrentHashMap<>();
    }

    @Override
    public boolean hasCaptcha(String captchaId) {
        return valueOps.get(captchaId) != null;
    }

    @Override
    public void storeCaptcha(String captchaId, Captcha captcha) throws CaptchaServiceException {
        this.valueOps.put(captchaId, captcha);
    }

    @Override
    public void storeCaptcha(String captchaId, Captcha captcha, Locale locale) throws CaptchaServiceException {
        this.valueOps.put(captchaId, new CaptchaAndLocale(captcha, locale));
    }

    @Override
    public boolean removeCaptcha(String captchaId) {
        if (this.hasCaptcha(captchaId)) {
            valueOps.remove(captchaId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Captcha getCaptcha(String captchaId) throws CaptchaServiceException {
        Object val = this.valueOps.get(captchaId);
        if (val == null) {
            return null;
        }
        if (val instanceof Captcha) {
            return (Captcha) val;
        }
        if (val instanceof CaptchaAndLocale) {
            return ((CaptchaAndLocale) val).getCaptcha();
        }
        return null;
    }

    @Override
    public Locale getLocale(String captchaId) throws CaptchaServiceException {
        Object captchaAndLocale = this.getCaptcha(captchaId);
        if (captchaAndLocale != null && captchaAndLocale instanceof CaptchaAndLocale) {
            return ((CaptchaAndLocale) captchaAndLocale).getLocale();
        }
        return null;
    }

    @Override
    public int getSize() {
        return this.valueOps.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection getKeys() {
        return valueOps.keySet();
    }

    @Override
    public void empty() {
        valueOps.clear();
    }

    @Override
    public void initAndStart() {
        this.empty();
    }

    @Override
    public void cleanAndShutdown() {
        this.empty();
    }
}
