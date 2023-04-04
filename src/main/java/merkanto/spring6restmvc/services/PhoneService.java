package merkanto.spring6restmvc.services;

import merkanto.spring6restmvc.model.PhoneDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhoneService {

    List<PhoneDTO> listPhones();

    Optional<PhoneDTO> getPhoneById(UUID id);

    PhoneDTO saveNewPhone(PhoneDTO phone);

    void updatePhoneById(UUID phoneId, PhoneDTO phone);

    void deleteById(UUID phoneId);

    void patchPhoneById(UUID phoneId, PhoneDTO phone);
}
