package com.milla.inventario.dto.stock;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ActualizarStockDTO {
    @NotNull(message = "Cantidad es requerida")
    @PositiveOrZero(message = "Cantidad no puede ser negativa")
    private Integer cantidad;
}
