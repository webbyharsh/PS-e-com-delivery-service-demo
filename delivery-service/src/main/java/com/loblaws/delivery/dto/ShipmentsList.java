package com.loblaws.delivery.dto;


import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ShipmentsList {

    private Long orderId;
    private List<String> shipmentIds;
}
