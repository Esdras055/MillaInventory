package com.milla.inventario.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milla.inventario.dto.usuario.AuthResponseDTO;
import com.milla.inventario.dto.usuario.LoginDTO;
import com.milla.inventario.service.IAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Operaciones de autenticacion y generacion de JWT")
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Autentica un usuario y devuelve un token JWT valido.")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
//Login
