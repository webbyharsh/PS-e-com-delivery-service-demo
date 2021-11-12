package com.loblaws.delivery.controller;


import com.loblaws.delivery.DeliveryServiceApplication;
import com.loblaws.delivery.dto.ShipmentsList;
import com.loblaws.delivery.entity.OrderDetails;
import com.loblaws.delivery.entity.Shipment;
import com.loblaws.delivery.repository.OrderDetailsRepository;
import com.loblaws.delivery.repository.ShipmentRepository;
import com.loblaws.delivery.service.IDeliveryService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = DeliveryServiceApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class DeliveryControllerIntTests {

    @Autowired
    private IDeliveryService deliveryService;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private DeliveryController deliveryController;

    @Autowired
    MockMvc mockMvc;

    private String shipmentIdForTest = "";
    private static List<Shipment> shipmentList = new ArrayList<Shipment>();
    private static List<OrderDetails> orderList = new ArrayList<OrderDetails>();




    @BeforeEach
    public void setUp(){
        OrderDetails orderDetails1 = new OrderDetails(1001L, "TestUser2", "TestAddress2", null);
        OrderDetails orderDetailsSaved = deliveryService.saveOrderDetails(orderDetails1);
        orderList.add(orderDetailsSaved);
        Shipment shipment = deliveryService.generateShipmentIdForOrder(orderDetails1);
        shipmentIdForTest = shipment.getShipmentId();
        shipmentList.add(shipment);
        Shipment shipment1 = deliveryService.generateShipmentIdForOrder(orderDetails1);
        shipmentList.add(shipment1);
    }


    @Test
    public void testGetShipment(){
        ResponseEntity<Shipment> response = deliveryController.getShipment(shipmentIdForTest);
        Assert.assertEquals(response.getBody().getOrderDetails().getOrderId(), (Long)1001L);
        Assert.assertEquals(response.getStatusCodeValue(), 200);
    }
    @Test
    public void testGetShipmentWhenNoShipment(){
        ResponseEntity<Shipment> response = deliveryController.getShipment("abcd-random-string");
        Assert.assertEquals(response.getStatusCodeValue(), 404);
    }

    @Test
    public void testGetShipments(){
        ResponseEntity<ShipmentsList> response = deliveryController.getShipmentsForOrder(1001L);
        Assert.assertEquals(response.getBody().getShipmentIds().size(), shipmentList.size());
    }

    @Test
    void testGetShipmentEndpoint() throws Exception{
        String test =  deliveryController.getShipment(shipmentIdForTest).toString();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/delivery/" + shipmentIdForTest)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testGetShipmentWhenNoShipmentEndpoint() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/delivery/"+"abd-random")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetShipmentsEndpoint() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/delivery/shipments/" + 1001)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
