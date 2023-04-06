package merkanto.spring6restmvc.services;

import lombok.RequiredArgsConstructor;
import merkanto.spring6restmvc.entities.mappers.PhoneMapper;
import merkanto.spring6restmvc.model.PhoneDTO;
import merkanto.spring6restmvc.repositories.PhoneRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class PhoneServiceJPA implements PhoneService {
    private final PhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper;

    @Override
    public List<PhoneDTO> listPhones() {
        return phoneRepository.findAll()
                .stream()
                .map(phoneMapper::phoneToPhoneDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PhoneDTO> getPhoneById(UUID id) {
        return Optional.ofNullable(phoneMapper.phoneToPhoneDto(phoneRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public PhoneDTO saveNewPhone(PhoneDTO phone) {
        return phoneMapper.phoneToPhoneDto(phoneRepository.save(phoneMapper.phoneDtoToPhone(phone)));
    }

    @Override
    public Optional<PhoneDTO> updatePhoneById(UUID phoneId, PhoneDTO phone) {
        AtomicReference<Optional<PhoneDTO>> atomicReference = new AtomicReference<>();

        phoneRepository.findById(phoneId).ifPresentOrElse(foundPhone -> {
            foundPhone.setPhoneName(phone.getPhoneName());
            foundPhone.setPhoneStyle(phone.getPhoneStyle());
            foundPhone.setImei(phone.getImei());
            foundPhone.setPrice(phone.getPrice());
            atomicReference.set(Optional.of(phoneMapper
                    .phoneToPhoneDto(phoneRepository.save(foundPhone))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public Boolean deleteById(UUID phoneId) {
        if (phoneRepository.existsById(phoneId)) {
            phoneRepository.deleteById(phoneId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<PhoneDTO> patchPhoneById(UUID phoneId, PhoneDTO phone) {
        AtomicReference<Optional<PhoneDTO>> atomicReference = new AtomicReference<>();

        phoneRepository.findById(phoneId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(phone.getPhoneName())){
                foundBeer.setPhoneName(phone.getPhoneName());
            }
            if (phone.getPhoneStyle() != null){
                foundBeer.setPhoneStyle(phone.getPhoneStyle());
            }
            if (StringUtils.hasText(phone.getImei())){
                foundBeer.setImei(phone.getImei());
            }
            if (phone.getPrice() != null){
                foundBeer.setPrice(phone.getPrice());
            }
            if (phone.getQuantityOnHand() != null){
                foundBeer.setQuantityOnHand(phone.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(phoneMapper
                    .phoneToPhoneDto(phoneRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
