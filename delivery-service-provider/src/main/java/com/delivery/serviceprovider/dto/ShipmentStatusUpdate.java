package com.delivery.serviceprovider.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShipmentStatusUpdate {
    private String shipmentId;
    private Long orderId;
    private String status;
}
