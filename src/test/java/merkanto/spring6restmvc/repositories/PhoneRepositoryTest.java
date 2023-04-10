package merkanto.spring6restmvc.repositories;

import jakarta.validation.ConstraintViolationException;
import merkanto.spring6restmvc.bootstrap.BootstrapData;
import merkanto.spring6restmvc.entities.Phone;
import merkanto.spring6restmvc.model.PhoneStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(BootstrapData.class)
class PhoneRepositoryTest {

    @Autowired
    PhoneRepository phoneRepository;

    @Test
    void testGetPhoneListByName() {
        Page<Phone> list = phoneRepository.findAllByPhoneNameIsLikeIgnoreCase("%APPLE%", null);

        assertThat(list.getContent().size()).isEqualTo(1);
    }

    @Test
    void testSavePhoneNameTooLong() {

        assertThrows(ConstraintViolationException.class, () -> {
            Phone savedPhone = phoneRepository.save(Phone.builder()
                    .phoneName("My Phone Phone Phone Phone My Phone Phone Phone Phone My Phone Phone Phone Phone My Phone Phone Phone Phone My Phone Phone Phone Phone")
                    .phoneStyle(PhoneStyle.NOKIA)
                    .imei("531781297733084")
                    .price(new BigDecimal("1100"))
                    .build());

            phoneRepository.flush();
        });
    }

    @Test
    void testSavePhone() {
        Phone savedPhone = phoneRepository.save(Phone.builder()
                .phoneName("My Phone")
                .phoneStyle(PhoneStyle.NOKIA)
                .imei("531781297733084")
                .price(new BigDecimal("1100"))
                .build());

        phoneRepository.flush();

        assertThat(savedPhone).isNotNull();
        assertThat(savedPhone.getId()).isNotNull();
    }
}