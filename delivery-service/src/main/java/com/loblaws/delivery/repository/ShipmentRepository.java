package com.loblaws.delivery.repository;

import com.loblaws.delivery.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, String> {
     Shipment findByShipmentId(String shipmentId);
     @Query("select s from Shipment s where s.orderDetails.orderId = :orderId")
     List<Shipment> findByOrderId(@Param("orderId")Long orderId);
     @Query("select s from Shipment s JOIN FETCH s.orderDetails where s.shipmentId = :shipmentId")
     Shipment findByShipmentIdEagerly(@Param("shipmentId") String shipmentId);
}
