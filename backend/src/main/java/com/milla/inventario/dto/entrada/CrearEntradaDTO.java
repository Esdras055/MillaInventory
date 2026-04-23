package com.milla.inventario.dto.entrada;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CrearEntradaDTO {
    @NotNull(message = "ProductoId es requerido")
    @Positive(message = "ProductoId debe ser mayor que cero")
    private Long productoId;
    @NotNull(message = "ProveedorId es requerido")
    @Positive(message = "ProveedorId debe ser mayor que cero")
    private Long proveedorId;
    @NotNull(message = "BodegaId es requerido")
    @Positive(message = "BodegaId debe ser mayor que cero")
    private Long bodegaId;
    @NotNull(message = "Fecha es requerida")
    private LocalDate fecha;
    @NotNull(message = "Precio de adquisicion es requerido")
    @Positive(message = "Precio de adquisicion debe ser mayor que cero")
    private BigDecimal precioAdquisicion;
    @NotNull(message = "Cantidad es requerida")
    @Positive(message = "Cantidad debe ser mayor que cero")
    private Integer cantidad;
}
