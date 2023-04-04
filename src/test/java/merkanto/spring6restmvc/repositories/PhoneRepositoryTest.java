package merkanto.spring6restmvc.repositories;

import merkanto.spring6restmvc.entities.Phone;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PhoneRepositoryTest {

    @Autowired
    PhoneRepository phoneRepository;

    @Test
    void testSavePhone() {
        Phone savedPhone = phoneRepository.save(Phone.builder()
                        .phoneName("My Phone")
                .build());

        assertThat(savedPhone).isNotNull();
        assertThat(savedPhone.getId()).isNotNull();
    }
}