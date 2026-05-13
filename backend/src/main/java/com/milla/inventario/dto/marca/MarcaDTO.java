package com.milla.inventario.dto.marca;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MarcaDTO {
    private Long id;
    private String nombre;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
