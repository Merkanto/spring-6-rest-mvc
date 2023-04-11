package merkanto.spring6restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class PhoneOrder {

    public PhoneOrder(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String customerRef, Customer customer, Set<PhoneOrderLine> phoneOrderLines, PhoneOrderShipment phoneOrderShipment) {
        this.id = id;
        this.version = version;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.customerRef = customerRef;
        this.setCustomer(customer);
        this.phoneOrderLines = phoneOrderLines;
        this.setPhoneOrderShipment(phoneOrderShipment);
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false )
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    public boolean isNew() {
        return this.id == null;
    }

    private String customerRef;

    @ManyToOne
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.getPhoneOrders().add(this);
    }

    public void setPhoneOrderShipment(PhoneOrderShipment phoneOrderShipment) {
        this.phoneOrderShipment = phoneOrderShipment;
        phoneOrderShipment.setPhoneOrder(this);
    }

    @OneToMany(mappedBy = "phoneOrder")
    private Set<PhoneOrderLine> phoneOrderLines;

    @OneToOne(cascade = CascadeType.PERSIST)
    private PhoneOrderShipment phoneOrderShipment;
}
