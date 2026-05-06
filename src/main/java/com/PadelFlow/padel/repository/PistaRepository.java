package com.PadelFlow.padel.repository;

import com.PadelFlow.padel.model.Pista;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PistaRepository extends JpaRepository<Pista, Long> {
    List<Pista> findByActivaTrue();
}