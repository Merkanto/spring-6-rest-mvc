package merkanto.spring6restmvc.services;

import merkanto.spring6restmvc.model.PhoneDTO;
import merkanto.spring6restmvc.model.PhoneStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface PhoneService {

    Page<PhoneDTO> listPhones(String phoneName, PhoneStyle phoneStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);

    Optional<PhoneDTO> getPhoneById(UUID id);

    PhoneDTO saveNewPhone(PhoneDTO phone);

    Optional<PhoneDTO> updatePhoneById(UUID phoneId, PhoneDTO phone);

    Boolean deleteById(UUID phoneId);

    Optional<PhoneDTO> patchPhoneById(UUID phoneId, PhoneDTO phone);
}
