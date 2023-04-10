package merkanto.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import merkanto.spring6restmvc.model.PhoneDTO;
import merkanto.spring6restmvc.services.PhoneService;
import merkanto.spring6restmvc.services.PhoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<PhoneDTO> phoneArgumentCaptor;

    @BeforeEach
    void setUp() {
        phoneServiceImpl = new PhoneServiceImpl();
    }

    @Test
    void testPatchPhone() throws Exception {
        PhoneDTO phone = phoneServiceImpl.listPhones(null, null, false, 1, 25).getContent().get(0);

        Map<String, Object> phoneMap = new HashMap<>();
        phoneMap.put("phoneName", "New Name");

        mockMvc.perform(patch(PhoneController.PHONE_PATH_ID, phone.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneMap)))
                .andExpect(status().isNoContent());

        verify(phoneService).patchPhoneById(uuidArgumentCaptor.capture(), phoneArgumentCaptor.capture());

        assertThat(phone.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(phoneMap.get("phoneName")).isEqualTo(phoneArgumentCaptor.getValue().getPhoneName());
    }

    @Test
    void testDeletePhone() throws Exception {
        PhoneDTO phone = phoneServiceImpl.listPhones(null, null, false, 1, 25).getContent().get(0);

        given(phoneService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(PhoneController.PHONE_PATH_ID, phone.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

        verify(phoneService).deleteById(uuidArgumentCaptor.capture());

        assertThat(phone.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdatePhone() throws Exception {
        PhoneDTO phone = phoneServiceImpl.listPhones(null, null, false, 1, 25).getContent().get(0);

        given(phoneService.updatePhoneById(any(), any())).willReturn(Optional.of(phone));

        mockMvc.perform(put(PhoneController.PHONE_PATH_ID, phone.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isNoContent());

        verify(phoneService).updatePhoneById(any(UUID.class), any(PhoneDTO.class));
    }

    @Test
    void testUpdatePhoneBlankName() throws Exception {
        PhoneDTO phone = phoneServiceImpl.listPhones(null, null, false, 1, 25).getContent().get(0);
        phone.setPhoneName("");

        given(phoneService.updatePhoneById(any(), any())).willReturn(Optional.of(phone));

        mockMvc.perform(put(PhoneController.PHONE_PATH_ID, phone.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testCreateNewPhone() throws Exception {
        PhoneDTO phone = phoneServiceImpl.listPhones(null, null, false, 1, 25).getContent().get(0);
        phone.setVersion(null);
        phone.setId(null);

        given(phoneService.saveNewPhone(any(PhoneDTO.class))).willReturn(phoneServiceImpl.listPhones(null, null, false, 1, 25).getContent().get(1));

        mockMvc.perform(post(PhoneController.PHONE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreatePhoneNullPhoneName() throws Exception {

        PhoneDTO phoneDTO = PhoneDTO.builder().build();

        given(phoneService.saveNewPhone(any(PhoneDTO.class))).willReturn(phoneServiceImpl.listPhones(null, null, false, 1, 25).getContent().get(1));

        MvcResult mvcResult = mockMvc.perform(post(PhoneController.PHONE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(6)))
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testListPhones() throws Exception {
        given(phoneService.listPhones(any(), any(), any(), any(), any())).willReturn(phoneServiceImpl.listPhones(null, null, false, 1, 25));

        mockMvc.perform(get("/api/v1/phone")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(11)));
    }

    @Test
    void getPhoneByIdNotFound() throws Exception {

        given(phoneService.getPhoneById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(PhoneController.PHONE_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPhoneById() throws Exception {

        PhoneDTO testPhone = phoneServiceImpl.listPhones(null, null, false, 1, 25).getContent().get(0);

        given(phoneService.getPhoneById(testPhone.getId())).willReturn(Optional.of(testPhone));

        mockMvc.perform(get(PhoneController.PHONE_PATH_ID, testPhone.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testPhone.getId().toString())))
                .andExpect(jsonPath("$.phoneName", is(testPhone.getPhoneName())));
    }
}