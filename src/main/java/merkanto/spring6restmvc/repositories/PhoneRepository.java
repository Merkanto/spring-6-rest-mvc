package merkanto.spring6restmvc.repositories;

import merkanto.spring6restmvc.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {
}
