package com.loblaws.delivery.service;

import com.loblaws.delivery.dto.ShipmentsList;
import com.loblaws.delivery.entity.OrderDetails;
import com.loblaws.delivery.entity.Shipment;
import com.loblaws.delivery.repository.OrderDetailsRepository;
import com.loblaws.delivery.repository.ShipmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
public class DeliveryServiceMockTests {

    @InjectMocks
    private DeliveryService deliveryService;

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private ShipmentRepository shipmentRepository;



    private OrderDetails orderDetails1;

    private Shipment shipment1;

    private Shipment shipment2;


    @BeforeEach
    void setup(){
        orderDetails1 = new OrderDetails(1001L, "TestUser2", "TestAddress2", null);

        shipment1 = new Shipment();
        shipment1.setShipmentId("abcd-abcd-abcd-abcd");
        shipment1.setStatus(Shipment.Status.CREATE_SHIPMENT);
        shipment1.setOrderDetails(orderDetails1);
        shipment2 = new Shipment();
        shipment2.setShipmentId("a-a-a-a");
        shipment2.setStatus(Shipment.Status.CREATE_SHIPMENT);
        shipment2.setOrderDetails(orderDetails1);
    }

//    Shipment generateShipmentIdForOrder(OrderDetails orderDetails);
//
//    Shipment getShipmentFromId(String shipmentId);
//
//    Shipment updateShipmentStatus(String shipmentId, String updatedStatus);
//
//    OrderDetails saveOrderDetails(OrderDetails orderDetails);
//
//    OrderDetails getOrderDetails(Long orderId);
//
//    ShipmentsList getShipments(Long orderId);

    @Test
    void testGenerateShipmentIdForOrder(){
        Long orderId = 1001L;



        Shipment shipmentForTest = deliveryService.generateShipmentIdForOrder(orderDetails1);

        //when(shipmentRepository.save(shipmentForTest)).thenReturn(shipmentForTest);

        Assertions.assertEquals(shipmentForTest.getOrderDetails(), orderDetails1);

    }

    @Test
    void testGetShipmentFromId(){
        String shipmentId = "abc-abc-abc-abc";

        when(shipmentRepository.findByShipmentIdEagerly(shipmentId)).thenReturn(shipment1);

        Assertions.assertEquals(deliveryService.getShipmentFromId(shipmentId), shipment1);

    }

    @Test
    void testUpdateShipmentStatus(){
        String shipmentId = "abc-abc-abc";
        String updatedStatus = "DELIVERED";

        when(shipmentRepository.findByShipmentId(shipmentId)).thenReturn(shipment1);
        shipment1.setStatus(Shipment.Status.DELIVERED);

        Assertions.assertEquals(deliveryService.updateShipmentStatus(shipmentId, "DELIVERED"), shipment1);
    }

    @Test
    void testUpdateShipmentStatusWhenShipmentIdNull(){
        String shipmentId = null;
        Assertions.assertEquals(deliveryService.updateShipmentStatus(shipmentId, "DELIVERED"), null);
    }

    @Test
    void testSaveOrderDetails(){
        Long orderId = 1001L;

        when(orderDetailsRepository.save(orderDetails1)).thenReturn(orderDetails1);

        Assertions.assertEquals(deliveryService.saveOrderDetails(orderDetails1), orderDetails1);

    }

    @Test
    void testGetOrderDetails(){
        Long orderId = 1001L;

        when(orderDetailsRepository.findByOrderId(orderId)).thenReturn(orderDetails1);

        Assertions.assertEquals(deliveryService.getOrderDetails(orderId), orderDetails1);
    }

    @Test
    void testGetShipments(){
        Long orderId = 1001L;

        List<Shipment> shipments = new ArrayList<Shipment>();
        shipments.add(shipment1);
        shipments.add(shipment2);
        List<String> shipmentIds = new ArrayList<String>();
        shipmentIds.add(shipment1.getShipmentId());
        shipmentIds.add(shipment2.getShipmentId());

        when(shipmentRepository.findByOrderId(orderId)).thenReturn(shipments);

        ShipmentsList shipmentsList = new ShipmentsList(orderId, shipmentIds);
        Assertions.assertEquals(deliveryService.getShipments(orderId).toString(), shipmentsList.toString());
    }
}
