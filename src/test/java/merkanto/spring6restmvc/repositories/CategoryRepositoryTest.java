package merkanto.spring6restmvc.repositories;

import merkanto.spring6restmvc.entities.Category;
import merkanto.spring6restmvc.entities.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PhoneRepository phoneRepository;

    Phone testPhone;

    @BeforeEach
    void setUp() {
        testPhone = phoneRepository.findAll().get(0);
    }

    @Transactional
    @Test
    void testAddCategory() {
        Category savedCat = categoryRepository.save(Category.builder()
                        .description("Monoblock")
                .build());

        testPhone.addCategory(savedCat);
        Phone savedPhone = phoneRepository.save(testPhone);
        System.out.println(savedPhone.getPhoneName());
    }
}