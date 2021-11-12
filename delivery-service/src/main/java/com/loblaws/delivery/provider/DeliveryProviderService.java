package com.loblaws.delivery.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class DeliveryProviderService {
    @Autowired
    private DeliveryProviderOutboundGateway messageGateway;

    private Logger logger = LoggerFactory.getLogger(DeliveryProviderService.class);




    public void updateMockShipmentStatus(String shipmentId, Long orderId, String emailId){
        try {
//            TimeUnit.SECONDS.sleep(10); //step 1
//            log.info("STEP 1 :  Change status of "+ shipmentId + " to PENDING");
//            ShipmentStatusUpdate shipmentStatusUpdate1 = new ShipmentStatusUpdate(shipmentId, orderId, "PENDING");
//            ObjectWriter ow1 = new ObjectMapper().writer().withDefaultPrettyPrinter();
//            String shipmentStatusJson1 = ow1.writeValueAsString(shipmentStatusUpdate1);
//            messageGateway.sendToDeliveryStatusUpdate(shipmentStatusJson1);

            TimeUnit.SECONDS.sleep(10); //step 2
            logger.info("STEP 2 :  Change status to delivered "+ shipmentId + " to DELIVERED");
            ShipmentStatusUpdate shipmentStatusUpdate2 = new ShipmentStatusUpdate(shipmentId, orderId, "DELIVERED", emailId);
            ObjectWriter ow2 = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String shipmentStatusJson2 = ow2.writeValueAsString(shipmentStatusUpdate2);
            messageGateway.sendToDeliveryStatusUpdate(shipmentStatusJson2);
        }catch (Exception ex){
            logger.warn(ex.getMessage());
        }
    }
}
