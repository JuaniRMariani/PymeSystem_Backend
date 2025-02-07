package com.example.pymesystem_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sale_product")
@Data
public class SaleProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sale_product_id")
    private String sale_id;

    @Column(name = "product_id", nullable = false)
    private String product_id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private double price;

}
