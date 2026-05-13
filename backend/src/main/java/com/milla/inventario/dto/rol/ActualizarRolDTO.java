package com.milla.inventario.dto.rol;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActualizarRolDTO {
    @Pattern(regexp = ".*\\S.*", message = "Nombre no puede estar vacio")
    private String nombre;
}
