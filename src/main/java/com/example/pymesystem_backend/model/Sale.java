package com.example.pymesystem_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sale")
@Data
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_id")
    private Long sale_id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "sale_date")
    private String sale_date;

    @Column(name = "payment_method")
    private String payment_method;

}
