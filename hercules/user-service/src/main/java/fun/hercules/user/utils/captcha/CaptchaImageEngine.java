package fun.hercules.user.utils.captcha;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.Color;
import java.awt.Font;

/**
 * 登录验证码生成器
 */
public class CaptchaImageEngine extends ListImageCaptchaEngine {
    @Override
    protected void buildInitialFactories() {
        WordGenerator wgen = new RandomWordGenerator("ABCDEFGHJKMNPQRSTUVWXYZ23456789");
        RandomRangeColorGenerator cgen = new RandomRangeColorGenerator(new int[]{0, 255}, new int[]{20, 100}, new
                int[]{20, 100});

        TextPaster textPaster = new RandomTextPaster(4, 5, cgen, Boolean.TRUE);

        BackgroundGenerator backgroundGenerator = new UniColorBackgroundGenerator(240, 50,
                new Color(245, 245, 245));

        Font[] fontsList = new Font[]{new Font("Helvetica", Font.TYPE1_FONT, 10), new Font("Arial", 0, 14),
                new Font("Vardana", 0, 17),};

        FontGenerator fontGenerator = new RandomFontGenerator(18, 30, fontsList);
        WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
        this.addFactory(new GimpyFactory(wgen, wordToImage));
    }
}
