package fun.hercules.order.order.clients.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(exclude = {"cellphone", "telephone"})
public class User {
    private String id;
    private String username;
    private String fullname;
    private String enterpriseId;
    private String cellphone;
    private String telephone;
    private String role;
}
