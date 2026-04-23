package com.milla.inventario.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.usuario.AuthResponseDTO;
import com.milla.inventario.dto.usuario.LoginDTO;
import com.milla.inventario.entity.Token;
import com.milla.inventario.entity.Usuario;
import com.milla.inventario.repository.TokenRepository;
import com.milla.inventario.repository.UsuarioRepository;
import com.milla.inventario.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final TokenRepository tokenRepository;

    @Override
    public AuthResponseDTO authenticate(LoginDTO request) {
        if (request == null
                || request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username y password son requeridos");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas", ex);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        String jwtToken = jwtService.generateToken(userDetails);

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

        usuario.setLastLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);

        saveToken(usuario, jwtToken);

        return new AuthResponseDTO(jwtToken, request.getUsername());
    }

    @Override
    public void logout(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token de autorización requerido");
        }

        String jwtToken = authorizationHeader.substring(7);
        Token storedToken = tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token no encontrado"));

        storedToken.setExpired(true);
        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);
    }

    private void saveToken(Usuario usuario, String jwtToken) {
        Token token = new Token();
        token.setUsuario(usuario);
        token.setToken(jwtToken);
        token.setExpired(false);
        token.setRevoked(false);
        token.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }
}
