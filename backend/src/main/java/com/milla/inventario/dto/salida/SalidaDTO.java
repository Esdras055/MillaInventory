package com.milla.inventario.dto.salida;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SalidaDTO {
    private Long id;
    private Long productoId;
    private String productoNombre;
    private Long bodegaId;
    private String bodegaNombre;
    private LocalDate fecha;
    private Integer cantidad;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
