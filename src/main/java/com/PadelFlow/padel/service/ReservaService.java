package com.PadelFlow.padel.service;

import com.PadelFlow.padel.dto.DisponibilidadResponse;
import com.PadelFlow.padel.dto.ReservaRequest;
import com.PadelFlow.padel.model.Reserva;
import com.PadelFlow.padel.model.Usuario;
import com.PadelFlow.padel.repository.PistaRepository;
import com.PadelFlow.padel.repository.ReservaRepository;
import com.PadelFlow.padel.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final PistaRepository pistaRepository;
    private final UsuarioRepository usuarioRepository;

    private static final List<String> HORAS = List.of(
            "09:00","10:30","12:00","13:30","16:00","17:30","19:00","20:30"
    );

    public List<DisponibilidadResponse> getDisponibilidad(Long pistaId, String fechaStr, String emailUsuario) {
        LocalDate fecha = LocalDate.parse(fechaStr);
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElseThrow();
        List<DisponibilidadResponse> resultado = new ArrayList<>();

        for (String hora : HORAS) {
            var reserva = reservaRepository
                    .buscarReserva(pistaId, LocalDate.parse(fechaStr), hora, "ACTIVA");

            if (reserva.isEmpty()) {
                resultado.add(new DisponibilidadResponse(hora, true, null));
            } else {
                boolean esMia = reserva.get().getUsuario().getId().equals(usuario.getId());
                Long miId = esMia ? reserva.get().getId() : null;
                resultado.add(new DisponibilidadResponse(hora, false, miId));
            }
        }
        return resultado;
    }

    public Reserva crearReserva(ReservaRequest req, String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElseThrow();
        var pista = pistaRepository.findById(req.getPistaId())
                .orElseThrow(() -> new RuntimeException("Pista no encontrada"));

        boolean ocupada = reservaRepository
                .buscarReserva(pista.getId(), LocalDate.parse(req.getFecha()), req.getHora(), "ACTIVA")
                .isPresent();

        if (ocupada) throw new RuntimeException("La pista ya está reservada en ese horario");

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setPista(pista);
        reserva.setFecha(LocalDate.parse(req.getFecha()));
        reserva.setHora(req.getHora());
        reserva.setEstado("ACTIVA");

        return reservaRepository.save(reserva);
    }

    @org.springframework.transaction.annotation.Transactional
    public void cancelarReserva(Long reservaId, String emailUsuario) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        if (!reserva.getUsuario().getEmail().equals(emailUsuario)) {
            throw new RuntimeException("No tienes permiso para cancelar esta reserva");
        }

        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }

    @org.springframework.transaction.annotation.Transactional
    public List<Reserva> getMisReservas(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario).orElseThrow();
        return reservaRepository.findByUsuarioOrderByFechaDescHoraDesc(usuario)
                .stream()
                .filter(r -> r.getEstado().equals("ACTIVA"))
                .toList();
    }
}