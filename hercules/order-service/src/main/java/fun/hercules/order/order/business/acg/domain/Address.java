package fun.hercules.order.order.business.acg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"mobile", "phone", "email", "address"})
public class Address {
    private String name;

    private String mobile;

    private String phone;

    private String email;

    private String companyName;

    private String postcode;

    private String country;

    private String abbr;

    private String province;

    private String provinceId;

    private String city;

    private String cityId;

    private String district;

    private String districtId;

    private String address;
}
