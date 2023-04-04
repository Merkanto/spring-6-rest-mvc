package merkanto.spring6restmvc.entities.mappers;

import merkanto.spring6restmvc.entities.Phone;
import merkanto.spring6restmvc.model.PhoneDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PhoneMapper {

    Phone phoneDtoToPhone(PhoneDTO dto);

    PhoneDTO phoneToPhoneDto(Phone phone);
}
