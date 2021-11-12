package com.loblaws.delivery.controller;

import com.loblaws.delivery.dto.ShipmentsList;
import com.loblaws.delivery.entity.OrderDetails;
import com.loblaws.delivery.entity.Shipment;
import com.loblaws.delivery.service.DeliveryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DeliveryControllerMockTests {

    @InjectMocks
    private DeliveryController deliveryController;

    @Mock
    private DeliveryService deliveryService;

    private String shipmentIdForTest = "";
    private List<Shipment> shipmentList = new ArrayList<Shipment>();
    private List<OrderDetails> orderList = new ArrayList<OrderDetails>();

    private OrderDetails orderDetails1;
    private Shipment shipment1;
    private Shipment shipment2;
    @BeforeEach
    public void setUp(){
          orderDetails1 = new OrderDetails(1001L, "TestUser2", "TestAddress2", null);
//        OrderDetails orderDetailsSaved = deliveryService.saveOrderDetails(orderDetails1);
//        orderList.add(orderDetailsSaved);
//        Shipment shipment = deliveryService.generateShipmentIdForOrder(orderDetails1);
//        shipmentIdForTest = shipment.getShipmentId();
//        shipmentList.add(shipment);
//        Shipment shipment1 = deliveryService.generateShipmentIdForOrder(orderDetails1);
//        shipmentList.add(shipment1);
        shipment1 = new Shipment();
        shipment1.setShipmentId("abcd-abcd-abcd-abcd");
        shipment1.setStatus(Shipment.Status.CREATE_SHIPMENT);
        shipment1.setOrderDetails(orderDetails1);
        shipment2 = new Shipment();
        shipment2.setShipmentId("xyz-xyz-xyz-xyz");
        shipment2.setStatus(Shipment.Status.CREATE_SHIPMENT);
        shipment2.setOrderDetails(orderDetails1);
    }

    @Test
    void testGetShipmentWhenShipmentIdisGiven(){
        String shipmentId = "abcd-abcd-abcd-abcd";

        when(deliveryService.getShipmentFromId(shipmentId)).thenReturn(shipment1);
        ResponseEntity<Shipment> response = ResponseEntity.status(HttpStatus.OK
        ).body(shipment1);


        Assertions.assertEquals(deliveryController.getShipment(shipmentId), response);
    }
    @Test
    void testGetShipmentWhenShipmentIdisNotAvailable(){
        String shipmentId = "abcd-abcd-abcd-aadf";

        when(deliveryService.getShipmentFromId(shipmentId)).thenReturn(null);
        ResponseEntity<Shipment> response = ResponseEntity.status(HttpStatus.NOT_FOUND
        ).body(null);


        Assertions.assertEquals(deliveryController.getShipment(shipmentId), response);
    }

    @Test
    void testGetShipmentsForOrder(){
        Long orderId = 1001L;

        List<String> shipmentIds = new ArrayList<String>();
        shipmentIds.add("abcd-abcd-abcd-abcd");
        shipmentIds.add("xyz-xyz-xyz-xyz");
        ShipmentsList shipmentsList = new ShipmentsList(orderId, shipmentIds);
        shipmentsList.setShipmentIds(shipmentIds);
        when(deliveryService.getShipments(orderId)).thenReturn(shipmentsList);
        ResponseEntity<ShipmentsList> response = ResponseEntity.status(HttpStatus.OK).body(shipmentsList);


        Assertions.assertEquals(deliveryController.getShipmentsForOrder(orderId), response);

    }

}
