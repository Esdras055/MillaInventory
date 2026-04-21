package com.milla.inventario.dto.producto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CrearProductoDTO {
    private String nombre;
    private Long categoriaId;
    private BigDecimal precio;
    private List<Long> marcaIds;
}
