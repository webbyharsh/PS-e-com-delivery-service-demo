package com.delivery.serviceprovider.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Getter
@Setter
@ToString
public class OrderDetails {
    private Long orderId;
    private String email;
    private String address;
    private Date startTime;
}
