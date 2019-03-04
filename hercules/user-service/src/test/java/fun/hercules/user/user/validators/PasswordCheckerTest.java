package fun.hercules.user.user.validators;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class PasswordCheckerTest {

    private PasswordChecker passwordChecker = new PasswordChecker();

    @Test
    public void shouldAllowPasswordWithAtLeastOneNumberAndOneAlphaAndOneSpecialCharacter() throws Exception {
        assertTrue(passwordChecker.check("a1!"));
        assertTrue(passwordChecker.check("fs1@jc.com"));
        assertTrue(passwordChecker.check("Password1!"));
    }

    @Test
    public void shouldDenyWeakPassword() throws Exception {
        assertFalse(passwordChecker.check("0!@#$%^&*()"));
        assertFalse(passwordChecker.check("123456!"));
    }

    @Test
    public void shouldDenyPasswordWithNonPrintableCharacters() throws Exception {
        assertFalse(passwordChecker.check("a1!\1"));
    }

    @Test
    public void shouldDenyPasswordWithLengthLessThanEight() throws Exception {
        assertFalse(passwordChecker.checkLength("a18889"));
    }
}