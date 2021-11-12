package com.loblaws.delivery.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "shipment_tracker")
@ToString
public class Shipment {
    @Id
    private String shipmentId;

    private Date dateCreated;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderDetails orderDetails;

    public enum Status {
        DELIVERED, PICKED, PENDING, CUSTOMER_UNAVAILABLE, CANCELLED, CREATE_SHIPMENT;
    }

    private Status status;

    @PrePersist
    public void onCreation() {
        this.status = Status.CREATE_SHIPMENT;
    }
}
