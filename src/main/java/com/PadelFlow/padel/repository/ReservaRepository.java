package com.PadelFlow.padel.repository;

import com.PadelFlow.padel.model.Reserva;
import com.PadelFlow.padel.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioOrderByFechaDescHoraDesc(Usuario usuario);

    @Query("SELECT r FROM Reserva r WHERE r.pista.id = :pistaId AND r.fecha = :fecha AND r.hora = :hora AND r.estado = :estado")
    Optional<Reserva> buscarReserva(@Param("pistaId") Long pistaId,
                                    @Param("fecha") LocalDate fecha,
                                    @Param("hora") String hora,
                                    @Param("estado") String estado);
}