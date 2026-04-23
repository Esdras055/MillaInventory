package com.milla.inventario.mapper;

import com.milla.inventario.dto.categoria.CategoriaDTO;
import com.milla.inventario.dto.categoria.CrearCategoriaDTO;
import com.milla.inventario.entity.Categoria;

public class CategoriaMapper {

    public static CategoriaDTO toDTO(Categoria categoria) {
        if (categoria == null) return null;

        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        dto.setCreatedAt(categoria.getCreatedAt());
        dto.setUpdatedAt(categoria.getUpdatedAt());
        return dto;
    }

    public static Categoria toEntity(CrearCategoriaDTO dto) {
        if (dto == null) return null;

        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setCreatedAt(java.time.LocalDateTime.now());
        categoria.setUpdatedAt(java.time.LocalDateTime.now());
        return categoria;
    }
}
