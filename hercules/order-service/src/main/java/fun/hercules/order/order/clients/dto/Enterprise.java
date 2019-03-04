package fun.hercules.order.order.clients.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(exclude = {"uniformSocialCreditCode", "certificateForUniformSocialCreditCode", "businessLicenseNumber",
        "certificateForBusinessLicense", "taxPayerNumber", "certificateForTaxRegistration", "organizationCode",
        "certificateForOrganization", "registrationAddress", "artificialPersonName", "artificialPersonContact"})
public class Enterprise {
    private String id;

    private String name;

    private String uniformSocialCreditCode;

    private String certificateForUniformSocialCreditCode;

    private String businessLicenseNumber;

    private String certificateForBusinessLicense;

    private String taxPayerNumber;

    private String certificateForTaxRegistration;

    private String organizationCode;

    private String certificateForOrganization;

    private String registrationAddress;

    private String artificialPersonName;

    private String artificialPersonContact;

    private ValidationStatus validationStatus;

    private Status status;

    public enum Status {
        ENABLED, DISABLED
    }

    public enum ValidationStatus {
        Unauthorized,
        AuthorizationInProcess,
        Authorized
    }
}
