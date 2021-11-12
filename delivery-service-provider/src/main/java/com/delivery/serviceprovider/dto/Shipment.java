package com.delivery.serviceprovider.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Getter
@Setter
@ToString
public class Shipment {
    private String shipmentId;

    private Date dateCreated;

    private OrderDetails orderDetails;

    public enum Status {
        DELIVERED, PICKED, PENDING, CUSTOMER_UNAVAILABLE, CANCELLED, CREATE_SHIPMENT;
    }

    private Status status;

}
