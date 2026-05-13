package com.milla.inventario.dto.stock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CrearBodegaProductoDTO {
    @NotNull(message = "ProductoId es requerido")
    @Positive(message = "ProductoId debe ser mayor que cero")
    private Long productoId;
    @NotNull(message = "BodegaId es requerido")
    @Positive(message = "BodegaId debe ser mayor que cero")
    private Long bodegaId;
    @NotNull(message = "Cantidad es requerida")
    @PositiveOrZero(message = "Cantidad no puede ser negativa")
    private Integer cantidad;
}
