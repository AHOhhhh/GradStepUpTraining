package fun.hercules.user.utils;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.AuthorizationException;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.validators.PasswordChecker;
import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.AuthorizationException;
import fun.hercules.user.common.exceptions.BadRequestException;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.validators.PasswordChecker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 用户验证工具
 */
public class VerifyUserUtils {
    private static PasswordChecker passwordChecker = new PasswordChecker();

    /**
     * 验证手机号
     *
     * @param user 用户信息，用来获取手机号
     */
    public static void verifyPhoneNumber(User user) {
        if (checkPhoneNumber(user.getTelephone()) && checkPhoneNumber(user.getCellphone())) {
            throw new BadRequestException(ErrorCode.MISSING_PHONE_NUMBER,
                    String.format("invalidate telephone or cellphone number"));
        }
    }

    /**
     * 手机号非空校验
     *
     * @param number 手机号码
     * @return 校验结果，布尔值
     */
    private static boolean checkPhoneNumber(String number) {
        return StringUtils.isEmpty(number == null ? null : number.trim());
    }

    public static void verifyFullname(User user) {
        if (StringUtils.isEmpty(user.getFullname())) {
            throw new BadRequestException(ErrorCode.MISSING_FULL_NAME);
        }
    }

    public static void verifyInitPassword(String password) {
        if (!(passwordChecker.checkLength(password) && passwordChecker.checkInitPassword(password))) {
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public static void checkResetAbility(User user) {
        if (user.isResettable()) {
            throw new AuthorizationException(ErrorCode.NO_AUTHORIZATION, "current user already init password!");
        }
    }

    public static void verifyResetPassword(String password) {
        if (!(passwordChecker.checkLength(password) && passwordChecker.check(password))) {
            throw new BadRequestException(ErrorCode.WEAK_PASSWORD,
                    String.format("invalidate password"));
        }
    }

    /**
     * 校验原始密码
     *
     * @param user             用户信息
     * @param originalPassword 重置密码时用户所输入的原始密码
     * @param passwordEncoder  密码编码器
     */
    public static void checkOriginalPassword(User user, String originalPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(originalPassword, user.getPassword())) {
            throw new AuthorizationException(ErrorCode.NO_AUTHORIZATION, "original password wrong!");
        }
    }
}
