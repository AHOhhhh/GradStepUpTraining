package fun.hercules.user.contact.repository;

import fun.hercules.user.contact.domain.Contact;
import fun.hercules.user.contact.domain.Contact;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    List<Contact> findByDeleted(Boolean isDeleted);

    List<Contact> findByCellphone(String cellphone);

    List<Contact> findByEnterpriseIdAndDeleted(String enterpriseId, boolean deleted, Pageable pageable);

    Contact findByIdAndDeleted(String contactId, boolean deleted);

    List<Contact> findByEnterpriseIdAndIsDefaultAndDeleted(String enterpriseId, boolean isDefault, boolean deleted);
}
