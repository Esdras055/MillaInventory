package com.milla.inventario.dto.producto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearProductoDTO {
    @NotBlank(message = "Nombre es requerido")
    private String nombre;
    @NotNull(message = "CategoriaId es requerido")
    @Positive(message = "CategoriaId debe ser mayor que cero")
    private Long categoriaId;
    @NotNull(message = "Precio es requerido")
    @Positive(message = "Precio debe ser mayor que cero")
    private BigDecimal precio;
    private List<Long> marcaIds;
}
