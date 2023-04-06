package merkanto.spring6restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    @NotNull
    private String phoneName;

    @NotNull
    private PhoneStyle phoneStyle;

    @NotNull
    @NotBlank
    private String imei;
    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
