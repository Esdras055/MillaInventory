package com.milla.inventario.dto.usuario;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String name;
    private String username;
    private boolean enabled;
    private boolean accountNonLocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
}
