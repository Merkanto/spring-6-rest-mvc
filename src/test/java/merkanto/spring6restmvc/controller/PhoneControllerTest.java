package merkanto.spring6restmvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import merkanto.spring6restmvc.model.Phone;
import merkanto.spring6restmvc.services.PhoneService;
import merkanto.spring6restmvc.services.PhoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhoneController.class)
class PhoneControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PhoneService phoneService;

    PhoneServiceImpl phoneServiceImpl;

    @BeforeEach
    void setUp() {
        phoneServiceImpl = new PhoneServiceImpl();
    }

    @Test
    void testCreatedNewPhone() throws Exception {

        Phone phone = phoneServiceImpl.listPhones().get(0);
        phone.setVersion(null);
        phone.setId(null);

        given(phoneService.saveNewPhone(any(Phone.class))).willReturn(phoneServiceImpl.listPhones().get(1));

        mockMvc.perform(post("/api/v1/phone")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testListPhones() throws Exception {
        given(phoneService.listPhones()).willReturn(phoneServiceImpl.listPhones());

        mockMvc.perform(get("/api/v1/phone")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getPhoneById() throws Exception {

        Phone testPhone = phoneServiceImpl.listPhones().get(0);

        given(phoneService.getPhoneById(testPhone.getId())).willReturn(testPhone);

        mockMvc.perform(get("/api/v1/phone/" + testPhone.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testPhone.getId().toString())))
                .andExpect(jsonPath("$.phoneName", is(testPhone.getPhoneName())));
    }
}