package merkanto.spring6restmvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import merkanto.spring6restmvc.model.Phone;
import merkanto.spring6restmvc.services.PhoneService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PhoneController {

    public static final String PHONE_ID = "phoneId";

    public static final String PHONE_PATH = "/api/v1/phone";

    public static final String PHONE_PATH_ID = PHONE_PATH + "/{" + PHONE_ID + "}";



    private final PhoneService phoneService;

    @PatchMapping(PHONE_PATH_ID)
    public ResponseEntity updatePhonePatchById(@PathVariable(PHONE_ID) UUID phoneId,
                                               @RequestBody Phone phone) {

        phoneService.patchPhoneById(phoneId, phone);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PHONE_PATH_ID)
    public ResponseEntity deleteById(@PathVariable(PHONE_ID) UUID phoneId){

        phoneService.deleteById(phoneId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(PHONE_PATH_ID)
    public ResponseEntity updateById(@PathVariable(PHONE_ID) UUID phoneId, @RequestBody Phone phone) {

        phoneService.updatePhoneById(phoneId, phone);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(PHONE_PATH)
    public ResponseEntity handlePost(@RequestBody Phone phone) {
        Phone savedPhone = phoneService.saveNewPhone(phone);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", PHONE_PATH + "/" + savedPhone.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = PHONE_PATH)
    public List<Phone> listPhones() {
        return phoneService.listPhones();
    }

    @GetMapping(value = PHONE_PATH_ID)
    public Phone getPhoneById(@PathVariable(PHONE_ID) UUID phoneId) {
        log.debug("Get Phone by Id - in controller");

        return phoneService.getPhoneById(phoneId);
    }
}
