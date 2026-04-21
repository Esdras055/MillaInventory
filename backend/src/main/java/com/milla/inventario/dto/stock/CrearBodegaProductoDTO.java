package com.milla.inventario.dto.stock;

import lombok.Data;

@Data
public class CrearBodegaProductoDTO {
    private Long productoId;
    private Long bodegaId;
    private Integer cantidad;
}
