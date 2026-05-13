package com.milla.inventario.dto.proveedor;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProveedorDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
