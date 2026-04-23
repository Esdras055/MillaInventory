package com.milla.inventario.dto.bodega;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BodegaDTO {
    private Long id;
    private String nombre;
    private Long ubicacionId;
    private String municipio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
