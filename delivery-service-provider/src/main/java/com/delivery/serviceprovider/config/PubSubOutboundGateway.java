package com.delivery.serviceprovider.config;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface PubSubOutboundGateway {
    @Gateway(requestChannel = "deliveryStatusUpdate")
    void sendToDeliveryStatusUpdate(String message);
}
