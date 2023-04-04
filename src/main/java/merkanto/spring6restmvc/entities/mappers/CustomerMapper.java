package merkanto.spring6restmvc.entities.mappers;

import merkanto.spring6restmvc.entities.Customer;
import merkanto.spring6restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
