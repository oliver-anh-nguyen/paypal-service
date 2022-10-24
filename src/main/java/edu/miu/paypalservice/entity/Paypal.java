package edu.miu.paypalservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Paypal {
    @PrimaryKey
    @Column
    private UUID id;
    @Column
    private String paymentType;
    @Column
    private String email;
    @Column
    private double total;
    @Column
    private int night;
    @Column
    private UUID propertyId;
}
