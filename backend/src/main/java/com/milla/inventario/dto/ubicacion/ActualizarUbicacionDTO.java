package com.milla.inventario.dto.ubicacion;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActualizarUbicacionDTO {
    @Pattern(regexp = ".*\\S.*", message = "Municipio no puede estar vacio")
    private String municipio;
}
