package com.milla.inventario.dto.salida;

import java.time.LocalDate;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ActualizarSalidaDTO {
    @Positive(message = "ProductoId debe ser mayor que cero")
    private Long productoId;
    @Positive(message = "BodegaId debe ser mayor que cero")
    private Long bodegaId;
    private LocalDate fecha;
    @Positive(message = "Cantidad debe ser mayor que cero")
    private Integer cantidad;
}
