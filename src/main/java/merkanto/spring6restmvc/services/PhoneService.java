package merkanto.spring6restmvc.services;

import merkanto.spring6restmvc.model.PhoneDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhoneService {

    List<PhoneDTO> listPhones();

    Optional<PhoneDTO> getPhoneById(UUID id);

    PhoneDTO saveNewPhone(PhoneDTO phone);

    Optional<PhoneDTO> updatePhoneById(UUID phoneId, PhoneDTO phone);

    Boolean deleteById(UUID phoneId);

    Optional<PhoneDTO> patchPhoneById(UUID phoneId, PhoneDTO phone);
}
