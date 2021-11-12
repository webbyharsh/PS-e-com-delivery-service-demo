package com.delivery.serviceprovider.config;

import com.delivery.serviceprovider.dto.Shipment;
import com.delivery.serviceprovider.service.DeliveryProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@Configuration
public class PubSubConfig {

    @Autowired
    private DeliveryProvider deliveryProvider;

    @Bean
    public MessageChannel shipmentCreatedForDelivery(){
        return new DirectChannel();
    }

    @Bean
    public PubSubInboundChannelAdapter shipmentCreatedInboundChannel(@Qualifier("shipmentCreatedForDelivery") MessageChannel messageChannel,
                                                                    PubSubTemplate pubSubTemplate){
        PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "delivery-shipment-ack");
        adapter.setOutputChannel(messageChannel);
        adapter.setAckMode(AckMode.AUTO);
        adapter.setPayloadType(String.class);
        return adapter;
    }

    @ServiceActivator(inputChannel = "shipmentCreatedForDelivery")
    public void orderFulfillMessageReceiver(
            String payload,
            @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) throws Exception{
        log.info("Shipment received - " + payload.toString());
        log.info("Dumping shipment in log file " + payload.toString());
        Shipment shipmentDetails = new ObjectMapper().readValue(payload, Shipment.class);
        log.info("Converted object " + shipmentDetails.toString());
        String shipmentId = shipmentDetails.getShipmentId();
        Long orderId = shipmentDetails.getOrderDetails().getOrderId();
        deliveryProvider.updateMockShipmentStatus(shipmentId, orderId);
    }

    @Bean
    @ServiceActivator(inputChannel = "deliveryStatusUpdate")
    public MessageHandler shipmentCreatedMessageSender(PubSubTemplate pubSubTemplate) {
        PubSubMessageHandler pubSubMessageHandler = new PubSubMessageHandler(pubSubTemplate, "shipment-status");
        //shipment-status topic provided here.
        pubSubMessageHandler.setPublishCallback(new ListenableFutureCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("There was an error publishing message to shipment-status topic");
            }

            @Override
            public void onSuccess(String result) {
                log.info("Message was sent via the outbound channel adapter to shipment-status topic");
            }
        });
        return pubSubMessageHandler;
    }
}
