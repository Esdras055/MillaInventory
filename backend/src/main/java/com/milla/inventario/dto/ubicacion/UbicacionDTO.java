package com.milla.inventario.dto.ubicacion;

import lombok.Data;

@Data
public class UbicacionDTO {
    private Long id;
    private String municipio;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
}
