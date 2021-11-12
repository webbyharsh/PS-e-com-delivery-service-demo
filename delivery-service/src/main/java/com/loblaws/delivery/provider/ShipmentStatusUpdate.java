package com.loblaws.delivery.provider;

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
    private String emailId;
}
