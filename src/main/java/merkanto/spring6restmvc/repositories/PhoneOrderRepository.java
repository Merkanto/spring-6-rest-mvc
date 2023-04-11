package merkanto.spring6restmvc.repositories;

import merkanto.spring6restmvc.entities.PhoneOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneOrderRepository extends JpaRepository<PhoneOrder, UUID> {
}
