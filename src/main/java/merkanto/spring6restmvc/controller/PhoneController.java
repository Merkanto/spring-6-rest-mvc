package merkanto.spring6restmvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import merkanto.spring6restmvc.model.PhoneDTO;
import merkanto.spring6restmvc.model.PhoneStyle;
import merkanto.spring6restmvc.services.PhoneService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
                                               @RequestBody PhoneDTO phone) {

        phoneService.patchPhoneById(phoneId, phone);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PHONE_PATH_ID)
    public ResponseEntity deleteById(@PathVariable(PHONE_ID) UUID phoneId){

        if(!phoneService.deleteById(phoneId)){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(PHONE_PATH_ID)
    public ResponseEntity updateById(@PathVariable(PHONE_ID) UUID phoneId, @Validated @RequestBody PhoneDTO phone) {

        if(phoneService.updatePhoneById(phoneId, phone).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(PHONE_PATH)
    public ResponseEntity handlePost(@Validated @RequestBody PhoneDTO phone) {
        PhoneDTO savedPhone = phoneService.saveNewPhone(phone);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", PHONE_PATH + "/" + savedPhone.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(value = PHONE_PATH)
    public Page<PhoneDTO> listPhones(@RequestParam(required = false) String phoneName,
                                     @RequestParam(required = false) PhoneStyle phoneStyle,
                                     @RequestParam(required = false) Boolean showInventory,
                                     @RequestParam(required = false) Integer pageNumber,
                                     @RequestParam(required = false) Integer pageSize) {
        return phoneService.listPhones(phoneName, phoneStyle, showInventory, pageNumber, pageSize);
    }

    @GetMapping(value = PHONE_PATH_ID)
    public PhoneDTO getPhoneById(@PathVariable(PHONE_ID) UUID phoneId) {
        log.debug("Get Phone by Id - in controller");

        return phoneService.getPhoneById(phoneId).orElseThrow(NotFoundException::new);
    }
}
