package com.milla.inventario.dto.rol;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearRolDTO {
    @NotBlank(message = "Nombre es requerido")
    private String nombre;
}
