package com.loblaws.delivery.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeliveryStatusUpdate {
    private String shipmentId;
    private Long orderId;
    private String status;
    private String emailId;
}
