package merkanto.spring6restmvc.repositories;

import merkanto.spring6restmvc.entities.Customer;
import merkanto.spring6restmvc.entities.Phone;
import merkanto.spring6restmvc.entities.PhoneOrder;
import merkanto.spring6restmvc.entities.PhoneOrderShipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PhoneOrderRepositoryTest {

    @Autowired
    PhoneOrderRepository phoneOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PhoneRepository phoneRepository;

    Customer testCustomer;
    Phone testPhone;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testPhone = phoneRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testPhoneOrders() {
        PhoneOrder phoneOrder = PhoneOrder.builder()
                .customerRef("Test order")
                .customer(testCustomer)
                .phoneOrderShipment(PhoneOrderShipment.builder()
                        .trackingNumber("1235r")
                        .build())
                .build();

        PhoneOrder savedPhoneOrder = phoneOrderRepository.save(phoneOrder);

        System.out.println(savedPhoneOrder.getCustomerRef());
    }
}