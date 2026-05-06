package com.PadelFlow.padel.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pista_id", nullable = false)
    private Pista pista;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String hora;

    @Column(nullable = false)
    private String estado = "ACTIVA";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}