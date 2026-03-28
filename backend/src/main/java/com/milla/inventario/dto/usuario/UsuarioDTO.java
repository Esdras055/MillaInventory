package com.milla.inventario.dto.usuario;

import java.util.Date;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String name;
    private String username;
    private boolean enabled;
    private boolean accountNonLocked;
    private Date createdAt;
    private Date updatedAt;
    private Date lastLogin;
}
