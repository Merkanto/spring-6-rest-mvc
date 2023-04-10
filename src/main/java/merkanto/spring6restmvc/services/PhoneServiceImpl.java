package merkanto.spring6restmvc.services;

import lombok.extern.slf4j.Slf4j;
import merkanto.spring6restmvc.model.PhoneDTO;
import merkanto.spring6restmvc.model.PhoneStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class PhoneServiceImpl implements PhoneService {

    private Map<UUID, PhoneDTO> phoneMap;

    public PhoneServiceImpl() {
        this.phoneMap = new HashMap<>();

        PhoneDTO phone1 = PhoneDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .phoneName("Apple")
                .phoneStyle(PhoneStyle.APPLE)
                .imei("358515950597612")
                .price(new BigDecimal("2000"))
                .quantityOnHand(212)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        PhoneDTO phone2 = PhoneDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .phoneName("Nokia")
                .phoneStyle(PhoneStyle.NOKIA)
                .imei("448815476353173")
                .price(new BigDecimal("1300"))
                .quantityOnHand(190)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        PhoneDTO phone3 = PhoneDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .phoneName("Samsung")
                .phoneStyle(PhoneStyle.SAMSUNG)
                .imei("445146876628227")
                .price(new BigDecimal("1800"))
                .quantityOnHand(200)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        phoneMap.put(phone1.getId(), phone1);
        phoneMap.put(phone2.getId(), phone2);
        phoneMap.put(phone3.getId(), phone3);
    }

    @Override
    public Page<PhoneDTO> listPhones(String phoneName, PhoneStyle phoneStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(new ArrayList<>(phoneMap.values()));
    }

    @Override
    public Optional<PhoneDTO> getPhoneById(UUID id) {
        log.debug("Get Phone by Id - in service. Id: " + id.toString());

        return Optional.of(phoneMap.get(id));
    }

    @Override
    public PhoneDTO saveNewPhone(PhoneDTO phone) {

        PhoneDTO savedPhone = PhoneDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .phoneName(phone.getPhoneName())
                .phoneStyle(phone.getPhoneStyle())
                .quantityOnHand(phone.getQuantityOnHand())
                .imei(phone.getImei())
                .price(phone.getPrice())
                .build();

        phoneMap.put(savedPhone.getId(), savedPhone);

        return savedPhone;
    }

    @Override
    public Optional<PhoneDTO> updatePhoneById(UUID phoneId, PhoneDTO phone) {
        PhoneDTO existing = phoneMap.get(phoneId);
        existing.setPhoneName(phone.getPhoneName());
        existing.setPrice(phone.getPrice());
        existing.setImei(phone.getImei());
        existing.setQuantityOnHand(phone.getQuantityOnHand());

        return Optional.of(existing);
    }

    @Override
    public Boolean deleteById(UUID phoneId) {
        phoneMap.remove(phoneId);
        return true;
    }

    @Override
    public Optional<PhoneDTO> patchPhoneById(UUID phoneId, PhoneDTO phone) {
        PhoneDTO existing = phoneMap.get(phoneId);

        if (StringUtils.hasText(phone.getPhoneName())){
            existing.setPhoneName(phone.getPhoneName());
        }

        if (phone.getPhoneStyle() != null) {
            existing.setPhoneStyle(phone.getPhoneStyle());
        }

        if (phone.getPrice() != null) {
            existing.setPrice(phone.getPrice());
        }

        if (phone.getQuantityOnHand() != null){
            existing.setQuantityOnHand(phone.getQuantityOnHand());
        }

        if (StringUtils.hasText(phone.getImei())) {
            existing.setImei(phone.getImei());
        }

        return Optional.of(existing);
    }
}
