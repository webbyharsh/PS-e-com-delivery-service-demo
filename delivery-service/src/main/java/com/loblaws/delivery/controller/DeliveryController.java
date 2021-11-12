package com.loblaws.delivery.controller;

import com.loblaws.delivery.dto.ShipmentsList;
import com.loblaws.delivery.entity.OrderDetails;
import com.loblaws.delivery.entity.Shipment;
import com.loblaws.delivery.exception.ErrorResponse;
import com.loblaws.delivery.service.IDeliveryService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery")
@Slf4j
public class DeliveryController {

    @Autowired
    private IDeliveryService deliveryService;


    @Operation(summary = "Get shipment details given the shipment Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment exists",content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Shipment.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Shipment does not exists"),
            @ApiResponse(responseCode = "500", description = "Api error occurred",content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/{shipmentId}")
    public ResponseEntity<Shipment> getShipment(@PathVariable("shipmentId") String shipmentId){
        Shipment shipment = deliveryService.getShipmentFromId(shipmentId);
        if(shipment != null){
            return ResponseEntity.status(HttpStatus.OK).body(shipment);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }

    @Operation(summary = "Get all the shipments that exists for that order Id. A order can have multiple shipments.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shipment(s) exists",content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ShipmentsList.class))}
            ),
            @ApiResponse(responseCode = "500", description = "Api error occurred",content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}
            )
    })
    @GetMapping("/shipments/{orderId}")
    public ResponseEntity<ShipmentsList> getShipmentsForOrder(@PathVariable("orderId") Long orderId){

        ShipmentsList shipmentsList = deliveryService.getShipments(orderId);
        log.info(shipmentsList.toString());
        return ResponseEntity.status(HttpStatus.OK).body(shipmentsList);
    }
}
