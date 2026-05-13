package com.milla.inventario.dto.entrada;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class EntradaDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private Long proveedorId;
    private String proveedorNombre;
    private Long bodegaId;
    private String bodegaNombre;
    private LocalDate fecha;
    private BigDecimal precioAdquisicion;
    private Integer cantidad;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
