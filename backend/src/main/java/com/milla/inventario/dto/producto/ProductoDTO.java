package com.milla.inventario.dto.producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.milla.inventario.dto.marca.MarcaDTO;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private Long categoriaId;
    private String categoriaNombre;
    private BigDecimal precio;
    private List<MarcaDTO> marcas;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
