package merkanto.spring6restmvc.controller;

import lombok.RequiredArgsConstructor;
import merkanto.spring6restmvc.model.CustomerDTO;
import merkanto.spring6restmvc.services.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMER_ID = "customerId";

    public static final String CUSTOMER_PATH = "/api/v1/customer";

    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{" + CUSTOMER_ID + "}";

    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomerById(@PathVariable(CUSTOMER_ID) UUID customerId,
                                            @RequestBody CustomerDTO customer) {

        customerService.patchCustomerById(customerId, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable(CUSTOMER_ID) UUID customerId){

        if (!customerService.deleteCustomerById(customerId)){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerById(@PathVariable(CUSTOMER_ID) UUID customerId,
                                             @RequestBody CustomerDTO customer) {

        if (customerService.updateCustomerById(customerId, customer).isEmpty()){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity handlePost(@RequestBody CustomerDTO customer) {
        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + "/" + savedCustomer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping(value = CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable(CUSTOMER_ID) UUID id){
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

}
