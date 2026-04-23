package com.milla.inventario.dto.marca;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearMarcaDTO {
    @NotBlank(message = "Nombre es requerido")
    private String nombre;
}
