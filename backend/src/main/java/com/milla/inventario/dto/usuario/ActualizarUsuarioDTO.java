package com.milla.inventario.dto.usuario;

import lombok.Data;

@Data
public class ActualizarUsuarioDTO {
    private String name;
    private String username;
    private String password;
    private Boolean enabled;
    private Boolean accountNonLocked;
}
