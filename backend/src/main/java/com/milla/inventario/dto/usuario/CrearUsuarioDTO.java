package com.milla.inventario.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearUsuarioDTO {
    @NotBlank(message = "Name es requerido")
    private String name;
    @NotBlank(message = "Username es requerido")
    private String username;
    @NotBlank(message = "Password es requerido")
    private String password;
}
