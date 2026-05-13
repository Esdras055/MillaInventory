package com.milla.inventario.dto.stock;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BodegaProductoDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private Long bodegaId;
    private String bodegaNombre;
    private Integer cantidad;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
