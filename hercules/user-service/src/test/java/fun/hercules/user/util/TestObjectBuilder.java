package fun.hercules.user.util;

import fun.hercules.user.enterprise.domain.Enterprise;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.utils.JsonUtils;
import fun.hercules.user.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestObjectBuilder {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User loadUserFromJsonData(String filename) throws IOException {
        User user = JsonUtils.getMapper().readValue(new ClassPathResource(String.format("/fixtures/user/%s", filename))
                .getFile(), User.class);
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return user;
    }

    public Enterprise loadEnterpriseFromJson(String filename) throws IOException {
        return JsonUtils.getMapper()
                .readValue(new ClassPathResource(String.format("/fixtures/enterprise/%s", filename))
                        .getFile(), Enterprise.class);
    }

}
