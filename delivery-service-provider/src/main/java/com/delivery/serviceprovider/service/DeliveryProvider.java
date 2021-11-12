package com.delivery.serviceprovider.service;

import com.delivery.serviceprovider.config.PubSubOutboundGateway;
import com.delivery.serviceprovider.dto.Shipment;
import com.delivery.serviceprovider.dto.ShipmentStatusUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DeliveryProvider {
    @Autowired
    private PubSubOutboundGateway messageGateway;

    public void updateMockShipmentStatus(String shipmentId, Long orderId){
        try {
//            TimeUnit.SECONDS.sleep(10); //step 1
//            log.info("STEP 1 :  Change status of "+ shipmentId + " to PENDING");
//            ShipmentStatusUpdate shipmentStatusUpdate1 = new ShipmentStatusUpdate(shipmentId, orderId, "PENDING");
//            ObjectWriter ow1 = new ObjectMapper().writer().withDefaultPrettyPrinter();
//            String shipmentStatusJson1 = ow1.writeValueAsString(shipmentStatusUpdate1);
//            messageGateway.sendToDeliveryStatusUpdate(shipmentStatusJson1);

            TimeUnit.SECONDS.sleep(10); //step 2
            log.info("STEP 2 :  Change status to delivered "+ shipmentId + " to DELIVERED");
            ShipmentStatusUpdate shipmentStatusUpdate2 = new ShipmentStatusUpdate(shipmentId, orderId, "DELIVERED");
            ObjectWriter ow2 = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String shipmentStatusJson2 = ow2.writeValueAsString(shipmentStatusUpdate2);
            messageGateway.sendToDeliveryStatusUpdate(shipmentStatusJson2);
        }catch (Exception ex){
            log.warn(ex.getMessage());
        }
    }
}
