package com.loblaws.delivery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.loblaws.delivery.dto.DeliveryStatusUpdate;
import com.loblaws.delivery.dto.NotificationPubSubDto;
import com.loblaws.delivery.dto.OrderPubSubDto;
import com.loblaws.delivery.entity.OrderDetails;
import com.loblaws.delivery.entity.Shipment;
import com.loblaws.delivery.service.IDeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.AckMode;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.cloud.gcp.pubsub.support.GcpPubSubHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class PubSubConfig {
    @Autowired
    private IDeliveryService deliveryService;
    @Autowired
    private PubSubOutboundGateway messageGateway;

    @Bean
    public MessageChannel shipmentStatusUpdate(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel orderFulfill(){
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter shipmentStatusInboundChannel(@Qualifier("shipmentStatusUpdate") MessageChannel messageChannel,
                                                             PubSubTemplate pubSubTemplate){
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "delivery-status-update");
        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.AUTO);
        adapter.setPayloadType(String.class);
        return adapter;
    }

    @Bean
    public PubSubInboundChannelAdapter orderFulfillInboundChannel(@Qualifier("orderFulfill") MessageChannel messageChannel,
                                                                    PubSubTemplate pubSubTemplate){
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "delivery-order-fulfill");
        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.AUTO);
        adapter.setPayloadType(String.class);
        return adapter;
    }

    @ServiceActivator(inputChannel = "shipmentStatusUpdate")
    public void updateStatusMessageReceiver(
            String payload,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
        log.info("Message arrived via an inbound channel adapter from delivery-status-update! Payload: " + payload);
        try {
            DeliveryStatusUpdate deliveryStatusUpdate = new ObjectMapper().readValue(payload, DeliveryStatusUpdate.class);
            log.info("Message received, updating shipment status");
            message.ack();
            deliveryService.updateShipmentStatus(deliveryStatusUpdate.getShipmentId(), deliveryStatusUpdate.getStatus());
            log.info("Shipment status updated");
            if(deliveryStatusUpdate.getStatus().equals("DELIVERED") ||
                    deliveryStatusUpdate.getStatus().equals("CUSTOMER_UNAVAILABLE")){
                log.info("Publishing message in Notification topic");
                Map<String, String> shipmentStatusStringMap = new HashMap<>();
                shipmentStatusStringMap.put("shipmentId", deliveryStatusUpdate.getShipmentId());
                shipmentStatusStringMap.put("orderId", deliveryStatusUpdate.getOrderId().toString());
                shipmentStatusStringMap.put("status", deliveryStatusUpdate.getStatus());
                shipmentStatusStringMap.put("emailId", deliveryStatusUpdate.getEmailId());
                NotificationPubSubDto notificationPubSubDto = new NotificationPubSubDto(
                        "deliveryService", shipmentStatusStringMap);
                ObjectWriter ow2 = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String notificationPubSubDtoString = ow2.writeValueAsString(notificationPubSubDto);
                log.info("Sending to notification topic " + notificationPubSubDtoString);
                messageGateway.sendToNotificationTopic(notificationPubSubDtoString);
            }
        }catch (Exception ex){
            log.warn(ex.toString());
        }
    }

    @ServiceActivator(inputChannel = "orderFulfill")
    public void orderFulfillMessageReceiver(
            String payload,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) throws Exception{
        log.info("Message arrived via an inbound channel adapter from delivery-order-fulfill! Payload: " + payload);
        log.info("Order fulfilled, creating shipment from the order created");
        //TODO: Creating OrderDetails object from payload and storing it in order_master table
        //Temporary code
        OrderPubSubDto orderPubSubDto = new ObjectMapper().readValue(payload, OrderPubSubDto.class);
        OrderDetails orderDetails = new OrderDetails(orderPubSubDto.getOrderId(), orderPubSubDto.getEmailId(), orderPubSubDto.getUserAddress(), orderPubSubDto.getOrderDate());
        if(deliveryService.getOrderDetails(orderDetails.getOrderId()) == null){
            deliveryService.saveOrderDetails(orderDetails);
            log.info("Order saved in order_master table");
        }else{
            log.info("Order already exists in database, creating new shipment");
        }
        Shipment shipment = deliveryService.generateShipmentIdForOrder(orderDetails);
        //TODO: add the shipment to create_shipment for delivery service provider
        log.info("Crating event for delivery provider service...");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String shipmentJson = ow.writeValueAsString(shipment);
        messageGateway.sendToShipmentCreatedTopic(shipmentJson);
        log.info("Order successfully sent to shipment-created topic");
    }

    @Bean
    @ServiceActivator(inputChannel = "notificationMessageChannel")
    public MessageHandler notificationMessageSender(PubSubTemplate pubSubTemplate){
        log.info("In Notification Message Sender");
        PubSubMessageHandler pubSubMessageHandler = new PubSubMessageHandler(pubSubTemplate, "notification");
        pubSubMessageHandler.setPublishCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("There was an error publishing message to notification topic.");
            }

            @Override
            public void onSuccess(String result) {
                log.info("Message was sent via the outbound channel adapter to notification topic");
            }
        });
        return pubSubMessageHandler;
    }
    @Bean
    @ServiceActivator(inputChannel = "shipmentCreatedMessageChannel")
    public MessageHandler shipmentCreatedMessageSender(PubSubTemplate pubSubTemplate) {
        PubSubMessageHandler pubSubMessageHandler = new PubSubMessageHandler(pubSubTemplate, "shipment-created");
        //delivery service GCP topic to be provided here.
        pubSubMessageHandler.setPublishCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("There was an error publishing message to shipment-created topic");
            }

            @Override
            public void onSuccess(String result) {
                log.info("Message was sent via the outbound channel adapter to shipment-created topic");
            }
        });
        return pubSubMessageHandler;
    }


}
