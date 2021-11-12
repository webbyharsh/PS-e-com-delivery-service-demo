//package com.loblaws.delivery.service;
//
//import com.loblaws.delivery.dto.ShipmentsList;
//import com.loblaws.delivery.entity.OrderDetails;
//import com.loblaws.delivery.entity.Shipment;
//import com.loblaws.delivery.repository.OrderDetailsRepository;
//import com.loblaws.delivery.repository.ShipmentRepository;
//import org.junit.jupiter.api.Order;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
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
//public class DeliveryServiceTest extends AbstractTestNGSpringContextTests {
//
//
//    @Autowired
//    private IDeliveryService deliveryService;
//
//    @Autowired
//    private ShipmentRepository shipmentRepository;
//
//    @Autowired
//    private OrderDetailsRepository orderDetailsRepository;
//
//    private String shipmentIdForTest = "";
//    private List<Shipment> shipmentList = new ArrayList<Shipment>();
//    private List<OrderDetails> orderList = new ArrayList<OrderDetails>();
//
//
//    @BeforeClass
//    public void setUp(){
//        OrderDetails orderDetails1 = new OrderDetails(1000L, "TestUser1", "TestAddress1", null);
//        OrderDetails orderDetailsSaved = deliveryService.saveOrderDetails(orderDetails1);
//        orderList.add(orderDetailsSaved);
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
//
//    }
//
//    @Test
//    public void testGetOrderDetails(){
//        OrderDetails orderDetails = deliveryService.getOrderDetails(1000L);
//        System.out.println(orderDetails.toString());
//    }
//
//    @Test
//    public void testGenerateShipmentIdFromOrder(){
//        OrderDetails orderDetails1 = new OrderDetails(1000L, "TestUser1", "TestAddress1", null);
//        Shipment shipment = deliveryService.generateShipmentIdForOrder(orderDetails1);
//        shipmentIdForTest = shipment.getShipmentId();
//        shipmentList.add(shipment);
//        if(shipment != null && shipment.getShipmentId().length() > 3 && shipment.getOrderDetails().getOrderId() == 1000L){
//            Assert.assertTrue(true);
//        }else{
//            Assert.assertFalse(true);
//        }
//    }
//
//    @Test(dependsOnMethods = "testGenerateShipmentIdFromOrder")
//    public void testGetShipmentFromId(){
//        Shipment shipment = deliveryService.getShipmentFromId(shipmentIdForTest);
//        Assert.assertEquals((Long)shipment.getOrderDetails().getOrderId(), (Long)1000L);
//    }
//
//    @Test(dependsOnMethods = "testGetShipmentFromId")
//    public void testListOfShipments(){
//        OrderDetails orderDetails1 = new OrderDetails(1000L, "TestUser1", "TestAddress1", null);
//        Shipment shipment = deliveryService.generateShipmentIdForOrder(orderDetails1);
//        shipmentList.add(shipment);
//        ShipmentsList shipmentsList = deliveryService.getShipments(1000L);
//        Assert.assertEquals(shipmentsList.getShipmentIds().size() , 2);
//    }
//}
