package com.milla.inventario.dto.usuario;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearUsuarioDTO {
    @NotBlank(message = "Name es requerido")
    private String name;
    @NotBlank(message = "Username es requerido")
    private String username;
    @NotBlank(message = "Password es requerido")
    private String password;
    private List<@Positive(message = "RoleId debe ser mayor que cero") Long> roleIds;
}
