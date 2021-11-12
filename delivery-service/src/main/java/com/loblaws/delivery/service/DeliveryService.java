package com.loblaws.delivery.service;

import com.loblaws.delivery.dto.ShipmentsList;
import com.loblaws.delivery.entity.OrderDetails;
import com.loblaws.delivery.entity.Shipment;
import com.loblaws.delivery.repository.OrderDetailsRepository;
import com.loblaws.delivery.repository.ShipmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DeliveryService implements IDeliveryService{

    @Autowired
    private ShipmentRepository shipmentRepository;


    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    public Shipment generateShipmentIdForOrder(OrderDetails orderDetails) {
        log.info("Generate shipment id for order " + orderDetails.toString());
        UUID shipmentUuid = UUID.randomUUID();
        String shipmentUuidString = shipmentUuid.toString();
        log.info("Generated uuid " + shipmentUuidString);
        Date date = new Date();
        Shipment shipment = new Shipment(shipmentUuidString, date, orderDetails, Shipment.Status.CREATE_SHIPMENT);
        shipmentRepository.save(shipment);
        log.info("Shipment saved in the shipment tracker table");
        return shipment;
    }

    @Override
    public Shipment getShipmentFromId(String shipmentId) {
        log.info("Get shipment from id service function");
        Shipment shipment = shipmentRepository.findByShipmentIdEagerly(shipmentId);
        return  shipment;
    }

    @Override
    public Shipment updateShipmentStatus(String shipmentId, String updatedStatus) {
        log.info("In update shipment status service function");
        if(shipmentId != null && updatedStatus != null){
            Shipment shipment = shipmentRepository.findByShipmentId(shipmentId);
            String status = updatedStatus.toUpperCase();
            shipment.setStatus(Shipment.Status.valueOf(status));
            shipmentRepository.save(shipment);
            log.info("Status of the shipment " + shipmentId + " successfully updated!");
            return shipment;
        }else{
            return null;
        }

    }

    @Override
    public OrderDetails saveOrderDetails(OrderDetails orderDetails) {
        if(orderDetails != null){
            orderDetailsRepository.save(orderDetails);
            return orderDetails;
        }
        return null;
    }

    @Override
    public OrderDetails getOrderDetails(Long orderId) {
        if(orderId != null){
            OrderDetails orderDetails = orderDetailsRepository.findByOrderId(orderId);
            return orderDetails;
        }
        return null;
    }

    @Override
    public ShipmentsList getShipments(Long orderId) {
        List<Shipment> listShipments = shipmentRepository.findByOrderId(orderId);
        List<String> shipmentIds = new ArrayList<String>();
        for(Shipment s : listShipments){
            shipmentIds.add(s.getShipmentId());
        }
        ShipmentsList shipmentsList = new ShipmentsList(orderId, shipmentIds);
        return shipmentsList;
    }


}
