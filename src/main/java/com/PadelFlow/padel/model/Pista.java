package com.PadelFlow.padel.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pistas")
public class Pista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activa = true;
}