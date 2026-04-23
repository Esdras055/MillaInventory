package com.milla.inventario.dto.entrada;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ActualizarEntradaDTO {
    @Positive(message = "ProductoId debe ser mayor que cero")
    private Long productoId;
    @Positive(message = "ProveedorId debe ser mayor que cero")
    private Long proveedorId;
    @Positive(message = "BodegaId debe ser mayor que cero")
    private Long bodegaId;
    private LocalDate fecha;
    @Positive(message = "Precio de adquisicion debe ser mayor que cero")
    private BigDecimal precioAdquisicion;
    @Positive(message = "Cantidad debe ser mayor que cero")
    private Integer cantidad;
}
