package com.milla.inventario.dto.categoria;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    private String nombre;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
