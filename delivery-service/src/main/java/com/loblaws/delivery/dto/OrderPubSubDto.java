package com.loblaws.delivery.dto;

import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderPubSubDto {
    private Long orderId;
    private Long userId;
    private String emailId;

    private OrderStatus orderStatus;
    private String userAddress;
    private Date orderDate;
    private Set<Items> orderItems=new HashSet<>();

    public enum OrderStatus {
        PENDING,
        FULFILLED,
        READY_To_DELIVER,
        DISPATCHED,
        CANCELED,
        DELIVERED;
    }
}
