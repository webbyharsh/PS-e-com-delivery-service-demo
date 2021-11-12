package com.loblaws.delivery.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "order_master")
public class OrderDetails {
    @Id
    private Long orderId;
    private String email;
    private String address;
    private Date startTime;
}
