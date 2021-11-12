package com.loblaws.delivery.service;

import com.loblaws.delivery.dto.ShipmentsList;
import com.loblaws.delivery.entity.OrderDetails;
import com.loblaws.delivery.entity.Shipment;
import org.hibernate.criterion.Order;

import java.util.List;

public interface IDeliveryService {

     Shipment generateShipmentIdForOrder(OrderDetails orderDetails);

     Shipment getShipmentFromId(String shipmentId);

     Shipment updateShipmentStatus(String shipmentId, String updatedStatus);

     OrderDetails saveOrderDetails(OrderDetails orderDetails);

     OrderDetails getOrderDetails(Long orderId);

     ShipmentsList getShipments(Long orderId);
}
