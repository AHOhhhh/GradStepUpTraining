package fun.hercules.user.contact.service;

import fun.hercules.user.common.errors.ErrorCode;
import fun.hercules.user.common.exceptions.ForbiddenException;
import fun.hercules.user.common.exceptions.NotFoundException;
import fun.hercules.user.contact.domain.Contact;
import fun.hercules.user.contact.repository.ContactRepository;
import fun.hercules.user.enterprise.service.EnterpriseService;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import fun.hercules.user.enterprise.service.EnterpriseService;
import fun.hercules.user.user.domain.User;
import fun.hercules.user.user.security.CurrentUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class ContactService {
    private final ContactRepository contactRepository;

    private final CurrentUserContext userContext;

    private final EnterpriseService enterpriseService;

    public ContactService(ContactRepository contactRepository,
                          CurrentUserContext userContext,
                          EnterpriseService enterpriseService) {
        this.contactRepository = contactRepository;
        this.userContext = userContext;
        this.enterpriseService = enterpriseService;
    }

    private boolean isEnterpriseUser() {
        return userContext.getUser().getRole().getName().equals("EnterpriseUser");
    }

    private boolean isEnterpriseAdmin() {
        return userContext.getUser().getRole().getName().equals("EnterpriseAdmin");
    }

    private boolean isPlatformAdmin() {
        return userContext.getUser().getRole().getName().equals("PlatformAdmin");
    }

    public boolean isPlatformService() {
        return userContext.getUser().getRole().getName().equals("PlatformService");
    }

    /**
     * 判断是否有权限操作企业联系人(防止平行越权)
     *
     * @param enterpriseId 企业ID
     * @return 布尔值，是否有权限操作企业联系人
     */
    private boolean canHandleEnterpriseContact(String enterpriseId) {
        return (isEnterpriseUser() || isEnterpriseAdmin())
                && userContext.getUser().getEnterpriseId().equals(enterpriseId)
                || isPlatformAdmin() || isPlatformService();
    }

    @Transactional
    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'PlatformService')")
    public String createContact(Contact contact) {
        User user = userContext.getUser();
        contact.setCreatedBy(user.getId());

        // 如果该企业不存在默认联系人，则设置为默认联系人
        if (contactRepository.findByEnterpriseIdAndIsDefaultAndDeleted(contact.getEnterpriseId(), true, false).size() == 0) {
            contact.setDefault(true);
        }

        Contact createdContact = contactRepository.save(contact);
        return createdContact.getId();
    }

    /**
     * 根据企业ID读取联系人，分页返回
     *
     * @param enterpriseId 企业ID
     * @param pageable     分页信息
     * @return 联系人列表
     */
    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'EnterpriseAdmin', 'PlatformAdmin', 'PlatformService')")
    public List<Contact> readContacts(String enterpriseId, Pageable pageable) {

        if (!canHandleEnterpriseContact(enterpriseId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
        return contactRepository.findByEnterpriseIdAndDeleted(enterpriseId, false, pageable);
    }

    /**
     * 根据contact id获取cotact
     *
     * @param contactId 联系人Id
     * @return 联系人信息
     */
    @PreAuthorize("hasAnyAuthority('EnterpriseUser', 'EnterpriseAdmin', 'PlatformAdmin')")
    public Contact readContactById(String contactId) {
        Contact contact = contactRepository.findByIdAndDeleted(contactId, false);

        if (contact == null) {
            throw new NotFoundException(ErrorCode.CONTACT_NOT_FOUND);
        }
        if (!canHandleEnterpriseContact(contact.getEnterpriseId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        return contact;
    }

    /**
     * 根据id找到contact并更新
     *
     * @param contactId 联系人Id
     * @param contact   最新的联系人信息
     */
    @PreAuthorize("hasAuthority('EnterpriseUser')")
    public void updateContactById(String contactId, Contact contact) {
        // 根据id获取原contact且未被删除的记录
        Contact oldContact = contactRepository.findByIdAndDeleted(contactId, false);

        if (oldContact == null) {
            throw new NotFoundException(ErrorCode.CONTACT_NOT_FOUND);
        }
        if (!canHandleEnterpriseContact(oldContact.getEnterpriseId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
        // 逐字段赋值为新contact的内容，ID不变。
        oldContact.setName(contact.getName());
        oldContact.setAddress(contact.getAddress());
        oldContact.setCellphone(contact.getCellphone());
        oldContact.setTelephone(contact.getTelephone());
        oldContact.setCountry(contact.getCountry());
        oldContact.setCountryAbbr(contact.getCountryAbbr());
        oldContact.setProvince(contact.getProvince());
        oldContact.setProvinceId(contact.getProvinceId());
        oldContact.setCity(contact.getCity());
        oldContact.setCityId(contact.getCityId());
        oldContact.setDistrict(contact.getDistrict());
        oldContact.setDistrictId(contact.getDistrictId());
        oldContact.setPostcode(contact.getPostcode());
        oldContact.setEmail(contact.getEmail());

        contactRepository.save(oldContact);
    }

    /**
     * 删除contact，标记删除（软删除）
     *
     * @param contactId 联系人Id
     */
    @PreAuthorize("hasAuthority('EnterpriseUser')")
    public void deleteContact(String contactId) {
        Contact contact = contactRepository.findOne(contactId);

        if (canHandleEnterpriseContact(contact.getEnterpriseId())) {
            contact.setDeleted(true);
            contactRepository.save(contact);
        }
    }

    /**
     * 设置contact 为默认
     *
     * @param contactId 联系人Id
     */
    @PreAuthorize("hasAuthority('EnterpriseUser')")
    public void setDefaultById(String contactId) {
        User user = userContext.getUser();
        Contact oldContact = contactRepository.findByIdAndDeleted(contactId, false);

        if (oldContact == null) {
            throw new NotFoundException(ErrorCode.CONTACT_NOT_FOUND);
        }
        if (!canHandleEnterpriseContact(oldContact.getEnterpriseId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        // 查到已有的default contact，取消其default属性，将新contact设置为默认contact
        Contact defaultContact = contactRepository.findByEnterpriseIdAndIsDefaultAndDeleted(user.getEnterpriseId(),
                true, false).get(0);
        defaultContact.setDefault(false);
        oldContact.setDefault(true);

        contactRepository.save(oldContact);
    }

    @PostConstruct
    public void onLoad() {
        String enterpriseId = enterpriseService.getEnterpriseByName("ThoughtWorks").getId();
        if (contactRepository.findByEnterpriseIdAndIsDefaultAndDeleted(enterpriseId, true, false).isEmpty()) {
            createContact(Contact.builder()
                    .enterpriseId(enterpriseId)
                    .name("小明")
                    .cellphone("13012345678")
                    .address("天谷八路环普产业园")
                    .isDefault(true)
                    .country("中国大陆")
                    .countryAbbr("CN")
                    .province("陕西省")
                    .provinceId("61")
                    .city("西安市")
                    .cityId("6101")
                    .district("雁塔区")
                    .districtId("610113")
                    .postcode("710000")
                    .email("xiaoming@thoughtworks.com")
                    .build());
        }
    }

}
