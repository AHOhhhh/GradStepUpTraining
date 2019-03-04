package fun.hercules.user.user.validators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class PasswordChecker {
    /**
     * 密码格式校验
     *
     * @param password 需要被校验的密码
     * @return 校验结果，布尔值
     */
    public boolean check(String password) {
        List<Integer> counters = getCounters(password);

        return counters.get(CharacterType.Other.ordinal()) == 0
                && counters.get(CharacterType.Alpha.ordinal()) > 0
                && counters.get(CharacterType.Numeric.ordinal()) > 0;
    }

    /**
     * 初始化密码时的规则校验
     *
     * @param password 需要被校验的初始化密码
     * @return 校验结果，布尔值
     */
    public boolean checkInitPassword(String password) {
        List<Integer> counters = getCounters(password);

        return counters.get(CharacterType.Other.ordinal()) == 0
                && counters.get(CharacterType.Alpha.ordinal()) > 0
                && counters.get(CharacterType.Numeric.ordinal()) > 0
                && counters.get(CharacterType.Special.ordinal()) > 0;
    }

    public boolean checkLength(String password) {
        return password.length() >= 8;
    }

    /**
     * 获取密码中每种字符出现的位置
     *
     * @param password 需要被校验的密码
     * @return 每种字符出现的位置列表
     */
    private List<Integer> getCounters(String password) {
        List<Integer> counters = new ArrayList<Integer>(Collections.nCopies(CharacterType.values().length, 0));

        int index = 0;
        for (Character ch : password.toCharArray()) {
            if (CharUtils.isAsciiAlpha(ch)) {
                index = CharacterType.Alpha.ordinal();
            } else if (CharUtils.isAsciiNumeric(ch)) {
                index = CharacterType.Numeric.ordinal();
            } else if (CharUtils.isAsciiPrintable(ch)) {
                index = CharacterType.Special.ordinal();
            } else {
                index = CharacterType.Other.ordinal();
                log.warn(String.format("password contains non-printable character \\%d", (int) ch));
            }
            counters.set(index, counters.get(index) + 1);
        }

        return counters;
    }

    private enum CharacterType {
        Alpha,
        Numeric,
        Special,
        Other
    }
}
