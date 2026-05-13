package com.milla.inventario.dto.categoria;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActualizarCategoriaDTO {
    @Pattern(regexp = ".*\\S.*", message = "Nombre no puede estar vacio")
    private String nombre;
}
