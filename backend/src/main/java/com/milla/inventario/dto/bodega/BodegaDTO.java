package com.milla.inventario.dto.bodega;

import lombok.Data;

@Data
public class BodegaDTO {
    private Long id;
    private String nombre;
    private Long ubicacionId;
    private String municipio;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;
}
