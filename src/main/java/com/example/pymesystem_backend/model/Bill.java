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
    private Long id;

    @OneToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(name = "bill_type", nullable = false)
    private String billType;

    @Column(name = "bill_number", nullable = false, unique = true)
    private String billNumber;

    @Column(name = "cae", nullable = false)
    private String cae;
}

