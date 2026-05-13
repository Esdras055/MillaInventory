package com.milla.inventario.dto.ubicacion;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UbicacionDTO {
    private Long id;
    private String municipio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
