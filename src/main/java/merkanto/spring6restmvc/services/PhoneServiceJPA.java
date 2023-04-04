package merkanto.spring6restmvc.services;

import lombok.RequiredArgsConstructor;
import merkanto.spring6restmvc.entities.mappers.PhoneMapper;
import merkanto.spring6restmvc.model.PhoneDTO;
import merkanto.spring6restmvc.repositories.PhoneRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class PhoneServiceJPA implements PhoneService {
    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    @Override
    public List<PhoneDTO> listPhones() {
        return null;
    }

    @Override
    public Optional<PhoneDTO> getPhoneById(UUID id) {
        return Optional.empty();
    }

    @Override
    public PhoneDTO saveNewPhone(PhoneDTO phone) {
        return null;
    }

    @Override
    public void updatePhoneById(UUID phoneId, PhoneDTO phone) {

    }

    @Override
    public void deleteById(UUID phoneId) {

    }

    @Override
    public void patchPhoneById(UUID phoneId, PhoneDTO phone) {

    }
}
