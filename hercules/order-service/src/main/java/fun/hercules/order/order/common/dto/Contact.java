package fun.hercules.order.order.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import fun.hercules.order.order.common.converter.GenericJsonValueConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Converter;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(exclude = {"id", "countryAbbr", "provinceId", "districtId", "cityId", "postcode"})
@ToString(exclude = {"cellphone", "telephone", "address"})
public class Contact implements Serializable {
    private String id;

    private String name;

    private String cellphone;

    private String telephone;

    private String email;

    private String country;

    private String countryAbbr;

    private String province;

    private String provinceId;

    private String city;

    private String cityId;

    private String district;

    private String districtId;

    private String address;

    private String postcode;

    @Converter
    public static class ContactsConverter extends GenericJsonValueConverter<List<Contact>> {
        public ContactsConverter() {
            super(new TypeReference<List<Contact>>() {
            });
        }
    }

    @Converter
    public static class ContactConverter extends GenericJsonValueConverter<Contact> {
        public ContactConverter() {
            super(new TypeReference<Contact>() {
            });
        }
    }
}
