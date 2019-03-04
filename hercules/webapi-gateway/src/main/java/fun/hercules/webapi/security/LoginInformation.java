package fun.hercules.webapi.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInformation {
    private String username;
    private String password;
    private String captcha;
    private String captchaId;
    private String roleType;
}
