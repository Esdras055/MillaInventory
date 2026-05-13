package com.milla.inventario.dto.bodega;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearBodegaDTO {
    @NotBlank(message = "Nombre es requerido")
    private String nombre;
    @NotNull(message = "UbicacionId es requerido")
    @Positive(message = "UbicacionId debe ser mayor que cero")
    private Long ubicacionId;
}
