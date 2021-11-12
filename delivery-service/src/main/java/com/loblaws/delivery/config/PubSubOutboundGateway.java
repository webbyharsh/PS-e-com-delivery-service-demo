package com.loblaws.delivery.config;

import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface PubSubOutboundGateway {
    @Gateway(requestChannel = "notificationMessageChannel")
    void sendToNotificationTopic(String message);

    @Gateway(requestChannel = "shipmentCreatedMessageChannel")
    void sendToShipmentCreatedTopic(String message);
}
