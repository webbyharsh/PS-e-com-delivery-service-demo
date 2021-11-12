//package com.loblaws.delivery.controller;
//
//import com.loblaws.delivery.dto.ShipmentsList;
//import com.loblaws.delivery.entity.OrderDetails;
//import com.loblaws.delivery.entity.Shipment;
//import com.loblaws.delivery.repository.OrderDetailsRepository;
//import com.loblaws.delivery.repository.ShipmentRepository;
//import com.loblaws.delivery.service.IDeliveryService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.testng.Assert;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//public class DeliveryControllerTest extends AbstractTestNGSpringContextTests {
//    @Autowired
//    private IDeliveryService deliveryService;
//
//    @Autowired
//    private ShipmentRepository shipmentRepository;
//
//    @Autowired
//    private OrderDetailsRepository orderDetailsRepository;
//
//    @Autowired
//    private DeliveryController deliveryController;
//
//    private String shipmentIdForTest = "";
//    private List<Shipment> shipmentList = new ArrayList<Shipment>();
//    private List<OrderDetails> orderList = new ArrayList<OrderDetails>();
//
//
//    @BeforeClass
//    public void setUp(){
//        OrderDetails orderDetails1 = new OrderDetails(1001L, "TestUser2", "TestAddress2", null);
//        OrderDetails orderDetailsSaved = deliveryService.saveOrderDetails(orderDetails1);
//        orderList.add(orderDetailsSaved);
//        Shipment shipment = deliveryService.generateShipmentIdForOrder(orderDetails1);
//        shipmentIdForTest = shipment.getShipmentId();
//        shipmentList.add(shipment);
//        Shipment shipment1 = deliveryService.generateShipmentIdForOrder(orderDetails1);
//        shipmentList.add(shipment1);
//    }
//
//    @AfterClass
//    public void clean(){
//        for(Shipment s : shipmentList){
//            shipmentRepository.delete(s);
//        }
//        for(OrderDetails o:orderList){
//            orderDetailsRepository.delete(o);
//        }
//    }
//
//    @Test
//    public void testGetShipmentEndpoint(){
//        ResponseEntity<Shipment> response = deliveryController.getShipment(shipmentIdForTest);
//        Assert.assertEquals(response.getBody().getOrderDetails().getOrderId(), (Long)1001L);
//        Assert.assertEquals(response.getStatusCodeValue(), 200);
//    }
//    @Test
//    public void testGetShipmentEndpointWhenNoShipment(){
//        ResponseEntity<Shipment> response = deliveryController.getShipment("abcd-random-string");
//        Assert.assertEquals(response.getStatusCodeValue(), 404);
//    }
//
//    @Test
//    public void testGetShipmentsEndpoint(){
//        ResponseEntity<ShipmentsList> response = deliveryController.getShipmentsForOrder(1001L);
//        Assert.assertEquals(response.getBody().getShipmentIds().size(), 2);
//    }
//}
