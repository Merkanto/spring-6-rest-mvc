package merkanto.spring6restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class PhoneDTO {

    private UUID id;
    private Integer version;
    private String phoneName;
    private PhoneStyle phoneStyle;
    private String imei;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
