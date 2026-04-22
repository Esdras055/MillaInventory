package com.milla.inventario.dto.entrada;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ActualizarEntradaDTO {
    private Long productoId;
    private Long proveedorId;
    private Long bodegaId;
    private LocalDate fecha;
    private BigDecimal precioAdquisicion;
    private Integer cantidad;
}
