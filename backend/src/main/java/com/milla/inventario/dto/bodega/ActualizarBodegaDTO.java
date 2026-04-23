package com.milla.inventario.dto.bodega;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ActualizarBodegaDTO {
    @Pattern(regexp = ".*\\S.*", message = "Nombre no puede estar vacio")
    private String nombre;
    @Positive(message = "UbicacionId debe ser mayor que cero")
    private Long ubicacionId;
}
