package merkanto.spring6restmvc.services;

import lombok.RequiredArgsConstructor;
import merkanto.spring6restmvc.entities.Phone;
import merkanto.spring6restmvc.entities.mappers.PhoneMapper;
import merkanto.spring6restmvc.model.PhoneDTO;
import merkanto.spring6restmvc.model.PhoneStyle;
import merkanto.spring6restmvc.repositories.PhoneRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<PhoneDTO> listPhones(String phoneName, PhoneStyle phoneStyle, Boolean showInventory,
                                     Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Phone> phonePage;

        if (StringUtils.hasText(phoneName) && phoneStyle == null) {
            phonePage = listPhonesByName(phoneName, pageRequest);
        } else if (!StringUtils.hasText(phoneName) && phoneStyle != null) {
            phonePage = listPhonesByStyle(phoneStyle, pageRequest);
        } else if (StringUtils.hasText(phoneName) && phoneStyle != null) {
            phonePage = listPhonesByNameAndStyle(phoneName, phoneStyle, pageRequest);
        } else {
            phonePage = phoneRepository.findAll(pageRequest);
        }

        if (showInventory != null && !showInventory) {
            phonePage.forEach(phone -> phone.setQuantityOnHand(null));
        }

        return phonePage.map(phoneMapper::phoneToPhoneDto);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("phoneName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Phone> listPhonesByNameAndStyle(String phoneName, PhoneStyle phoneStyle, Pageable pageable) {
        return phoneRepository.findAllByPhoneNameIsLikeIgnoreCaseAndPhoneStyle("%" + phoneName + "%",
                phoneStyle, pageable);
    }

    public Page<Phone> listPhonesByStyle(PhoneStyle phoneStyle, Pageable pageable) {
        return phoneRepository.findAllByPhoneStyle(phoneStyle, pageable);
    }

    public Page<Phone> listPhonesByName(String phoneName, Pageable pageable){
        return phoneRepository.findAllByPhoneNameIsLikeIgnoreCase("%" + phoneName + "%", pageable);
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
