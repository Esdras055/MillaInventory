package com.milla.inventario.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearCategoriaDTO {
    @NotBlank(message = "Nombre es requerido")
    private String nombre;
}
