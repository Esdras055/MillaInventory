package com.milla.inventario.dto.producto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ActualizarProductoDTO {
    @Pattern(regexp = ".*\\S.*", message = "Nombre no puede estar vacio")
    private String nombre;
    @Positive(message = "CategoriaId debe ser mayor que cero")
    private Long categoriaId;
    @Positive(message = "Precio debe ser mayor que cero")
    private BigDecimal precio;
    private List<Long> marcaIds;
}
