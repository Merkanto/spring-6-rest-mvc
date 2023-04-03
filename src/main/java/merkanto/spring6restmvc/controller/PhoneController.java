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
@RequestMapping("/api/v1/phone")
public class PhoneController {

    private final PhoneService phoneService;

    @PatchMapping("{phoneId}")
    public ResponseEntity updatePhonePatchById(@PathVariable("phoneId") UUID phoneId,
                                               @RequestBody Phone phone) {

        phoneService.patchPhoneById(phoneId, phone);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{phoneId}")
    public ResponseEntity deleteById(@PathVariable("phoneId") UUID phoneId){

        phoneService.deleteById(phoneId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{phoneId}")
    public ResponseEntity updateById(@PathVariable("phoneId") UUID phoneId, @RequestBody Phone phone) {

        phoneService.updatePhoneById(phoneId, phone);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity handlePost(@RequestBody Phone phone) {
        Phone savedPhone = phoneService.saveNewPhone(phone);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/phone/" + savedPhone.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Phone> listPhones() {
        return phoneService.listPhones();
    }

    @RequestMapping(value = "/{phoneId}", method = RequestMethod.GET)
    public Phone getPhoneById(@PathVariable("phoneId") UUID phoneId) {
        log.debug("Get Phone by Id - in controller");

        return phoneService.getPhoneById(phoneId);
    }
}
