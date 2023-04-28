package merkanto.spring6restmvc.mappers;

import merkanto.spring6restmvc.entities.Beer;
import merkanto.spring6restmvc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);

}
