package com.PadelFlow.padel.service;

import com.PadelFlow.padel.dto.*;
import com.PadelFlow.padel.model.Usuario;
import com.PadelFlow.padel.repository.UsuarioRepository;
import com.PadelFlow.padel.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Necesario para Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.builder()
                .username(u.getEmail())
                .password(u.getPassword())
                .roles(u.getRol().name())
                .build();
    }

    public TokenResponse register(RegisterRequest req) {
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        Usuario u = new Usuario();
        u.setNombre(req.getNombre());
        u.setApellido(req.getApellido());
        u.setEmail(req.getEmail());
        u.setTelefono(req.getTelefono());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        usuarioRepository.save(u);

        String token = jwtUtil.generateToken(u.getEmail());
        return new TokenResponse(token, u.getNombre(), u.getEmail());
    }

    public TokenResponse login(LoginRequest req) {
        Usuario u = usuarioRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtUtil.generateToken(u.getEmail());
        return new TokenResponse(token, u.getNombre(), u.getEmail());
    }
}