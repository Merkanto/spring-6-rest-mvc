package merkanto.spring6restmvc.bootstrap;

import merkanto.spring6restmvc.repositories.CustomerRepository;
import merkanto.spring6restmvc.repositories.PhoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BootstrapDataTest {

    @Autowired
    PhoneRepository phoneRepository;

    @Autowired
    CustomerRepository customerRepository;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(phoneRepository, customerRepository);
    }

    @Test
    void testRun() throws Exception {
        bootstrapData.run(null);

        assertThat(phoneRepository.count()).isEqualTo(3);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}