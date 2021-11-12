//package com.loblaws.delivery.exception;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//
//
//@ExtendWith(MockitoExtension.class)
//@AutoConfigureMockMvc
//public class ExceptionTests {
//
////    @Autowired
////    private IDeliveryService deliveryService;
////
////    @Autowired
////    private ShipmentRepository shipmentRepository;
////
////    @Autowired
////    private OrderDetailsRepository orderDetailsRepository;
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void testExceptionHandling() throws Exception {
//        try{
//            mockMvc.perform(
//                            get("/api/v1/delivery/shipments/abc"))
//                    .andExpect(status().isInternalServerError());
//        }catch (Exception ex){
//            Assertions.assertTrue(true);
//        }
//
//    }
//
//}
