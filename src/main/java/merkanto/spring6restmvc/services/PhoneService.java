package merkanto.spring6restmvc.services;

import merkanto.spring6restmvc.model.Phone;

import java.util.List;
import java.util.UUID;

public interface PhoneService {

    List<Phone> listPhones();

    Phone getPhoneById(UUID id);

    Phone saveNewPhone(Phone phone);

    void updatePhoneById(UUID phoneId, Phone phone);

    void deleteById(UUID phoneId);

    void patchPhoneById(UUID phoneId, Phone phone);
}
