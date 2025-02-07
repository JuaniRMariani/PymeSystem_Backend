package com.example.pymesystem_backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bill")
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Long bill_id;

    @Column(name = "sale_id", nullable = false)
    private Long sale_id;

    @Column(name = "bill_type", nullable = false)
    private String bill_type;

    @Column(name = "bill_number", nullable = false)
    private String bill_number;

    @Column(name = "cae", nullable = false)
    private String cae;

}
