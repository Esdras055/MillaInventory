package com.milla.inventario.mapper;

import java.util.stream.Collectors;

import com.milla.inventario.dto.producto.CrearProductoDTO;
import com.milla.inventario.dto.producto.ProductoDTO;
import com.milla.inventario.entity.Categoria;
import com.milla.inventario.entity.Producto;

public class ProductoMapper {

    public static ProductoDTO toDTO(Producto producto) {
        if (producto == null) return null;

        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        if (producto.getCategoria() != null) {
            dto.setCategoriaId(producto.getCategoria().getId());
            dto.setCategoriaNombre(producto.getCategoria().getNombre());
        }
        dto.setPrecio(producto.getPrecio());
        dto.setCreatedAt(producto.getCreatedAt());
        dto.setUpdatedAt(producto.getUpdatedAt());
        dto.setMarcas(producto.getMarcas().stream()
                .map(MarcaMapper::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Producto toEntity(CrearProductoDTO dto, Categoria categoria) {
        if (dto == null) return null;

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCategoria(categoria);
        producto.setPrecio(dto.getPrecio());
        producto.setCreatedAt(java.time.LocalDateTime.now());
        producto.setUpdatedAt(java.time.LocalDateTime.now());
        return producto;
    }
}
