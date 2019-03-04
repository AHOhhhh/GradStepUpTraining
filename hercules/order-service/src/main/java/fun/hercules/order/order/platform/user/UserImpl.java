package fun.hercules.order.order.platform.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;


@Data
@AllArgsConstructor
public class UserImpl implements User {

    private String userId;

    private String enterpriseId;

    private String role;

    private Set<Privilege> privileges;
}