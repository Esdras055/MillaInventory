package com.milla.inventario.dto.ubicacion;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearUbicacionDTO {
    @NotBlank(message = "Municipio es requerido")
    private String municipio;
}
