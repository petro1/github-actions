package org.acme.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @CreationTimestamp
    @Column(name = "created_on")
    private Timestamp createdOn;
}
