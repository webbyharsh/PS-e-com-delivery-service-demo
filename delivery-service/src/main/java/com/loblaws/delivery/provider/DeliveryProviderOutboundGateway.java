package com.loblaws.delivery.provider;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface DeliveryProviderOutboundGateway {
    @Gateway(requestChannel = "deliveryStatusUpdate")
    void sendToDeliveryStatusUpdate(String message);
}
