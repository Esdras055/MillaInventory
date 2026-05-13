package com.milla.inventario.dto.salida;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearSalidaDTO {
    @NotNull(message = "ProductoId es requerido")
    @Positive(message = "ProductoId debe ser mayor que cero")
    private Long productoId;
    @NotNull(message = "BodegaId es requerido")
    @Positive(message = "BodegaId debe ser mayor que cero")
    private Long bodegaId;
    @NotNull(message = "Fecha es requerida")
    private LocalDate fecha;
    @NotNull(message = "Cantidad es requerida")
    @Positive(message = "Cantidad debe ser mayor que cero")
    private Integer cantidad;
}
