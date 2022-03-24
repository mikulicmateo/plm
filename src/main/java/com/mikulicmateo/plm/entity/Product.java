package com.mikulicmateo.plm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "code", unique = true,length = 10)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "price_hrk")
    private double priceHrk;

    @Column(name = "price_eur")
    private double priceEur;

    @Column(name = "description")
    private String description;

    @Column(name = "is_available")
    private boolean available;

}
