package com.PadelFlow.padel.controller;

import com.PadelFlow.padel.dto.DisponibilidadResponse;
import com.PadelFlow.padel.dto.ReservaRequest;
import com.PadelFlow.padel.model.Reserva;
import com.PadelFlow.padel.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping("/disponibilidad")
    public List<DisponibilidadResponse> disponibilidad(
            @RequestParam Long pistaId,
            @RequestParam String fecha,
            @AuthenticationPrincipal UserDetails user) {
        return reservaService.getDisponibilidad(pistaId, fecha, user.getUsername());
    }

    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody ReservaRequest req,
            @AuthenticationPrincipal UserDetails user) {
        try {
            Reserva r = reservaService.crearReserva(req, user.getUsername());
            return ResponseEntity.ok(Map.of("id", r.getId(), "mensaje", "Reserva creada"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails user) {
        try {
            reservaService.cancelarReserva(id, user.getUsername());
            return ResponseEntity.ok(Map.of("mensaje", "Reserva cancelada"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", e.getMessage()));
        }
    }

    @GetMapping("/mis-reservas")
    public ResponseEntity<?> misReservas(
            @AuthenticationPrincipal UserDetails user) {
        List<Map<String, Object>> result = reservaService.getMisReservas(user.getUsername()).stream()
                .map(r -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id",          r.getId());
                    map.put("pistaNombre", r.getPista().getNombre());
                    map.put("fecha",       r.getFecha().toString());
                    map.put("hora",        r.getHora());
                    map.put("estado",      r.getEstado());
                    return map;
                })
                .toList();
        return ResponseEntity.ok(result);
    }
}