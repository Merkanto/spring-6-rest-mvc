package merkanto.spring6restmvc.bootstrap;

import lombok.RequiredArgsConstructor;
import merkanto.spring6restmvc.entities.Customer;
import merkanto.spring6restmvc.entities.Phone;
import merkanto.spring6restmvc.model.PhoneStyle;
import merkanto.spring6restmvc.repositories.CustomerRepository;
import merkanto.spring6restmvc.repositories.PhoneRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final PhoneRepository phoneRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadPhoneData();
        loadCustomerData();
    }

    private void loadPhoneData() {
        if (phoneRepository.count() == 0) {
            Phone phone1 = Phone.builder()
                    .phoneName("Apple")
                    .phoneStyle(PhoneStyle.APPLE)
                    .imei("358515950597612")
                    .price(new BigDecimal("2000"))
                    .quantityOnHand(212)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Phone phone2 = Phone.builder()
                    .phoneName("Nokia")
                    .phoneStyle(PhoneStyle.NOKIA)
                    .imei("448815476353173")
                    .price(new BigDecimal("1300"))
                    .quantityOnHand(190)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Phone phone3 = Phone.builder()
                    .phoneName("Samsung")
                    .phoneStyle(PhoneStyle.SAMSUNG)
                    .imei("445146876628227")
                    .price(new BigDecimal("1800"))
                    .quantityOnHand(200)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            phoneRepository.save(phone1);
            phoneRepository.save(phone2);
            phoneRepository.save(phone3);
        }
    }

    private void loadCustomerData() {
        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("Customer 1")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Customer customer2 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("Customer 2")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Customer customer3 = Customer.builder()
                    .id(UUID.randomUUID())
                    .name("Customer 3")
                    .version(1)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
        }
    }
}
