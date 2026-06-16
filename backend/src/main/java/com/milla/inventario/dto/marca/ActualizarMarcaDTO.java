package com.milla.inventario.dto.marca;

import java.util.List;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActualizarMarcaDTO {
    @Pattern(regexp = ".*\\S.*", message = "Nombre no puede estar vacio")
    private String nombre;
    private List<Long> categoriaIds;
    private List<Long> proveedorIds;
}
