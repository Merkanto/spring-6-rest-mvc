package merkanto.spring6restmvc.repositories;

import merkanto.spring6restmvc.entities.Phone;
import merkanto.spring6restmvc.model.PhoneStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {

    Page<Phone> findAllByPhoneNameIsLikeIgnoreCase(String phoneName, Pageable pageable);

    Page<Phone> findAllByPhoneStyle(PhoneStyle phoneStyle, Pageable pageable);

    Page<Phone> findAllByPhoneNameIsLikeIgnoreCaseAndPhoneStyle(String phoneName, PhoneStyle phoneStyle, Pageable pageable);
}
