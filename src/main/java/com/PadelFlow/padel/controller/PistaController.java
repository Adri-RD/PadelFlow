package com.PadelFlow.padel.controller;

import com.PadelFlow.padel.model.Pista;
import com.PadelFlow.padel.repository.PistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pistas")
@RequiredArgsConstructor
public class PistaController {

    private final PistaRepository pistaRepository;

    @GetMapping
    public List<Pista> getPistas() {
        return pistaRepository.findByActivaTrue();
    }
}
