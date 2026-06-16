package com.milla.inventario.mapper;

import java.util.stream.Collectors;

import com.milla.inventario.dto.marca.CrearMarcaDTO;
import com.milla.inventario.dto.marca.MarcaDTO;
import com.milla.inventario.entity.Marca;

public class MarcaMapper {

    public static MarcaDTO toDTO(Marca marca) {
        if (marca == null) return null;

        MarcaDTO dto = new MarcaDTO();
        dto.setId(marca.getId());
        dto.setNombre(marca.getNombre());
        dto.setCreatedAt(marca.getCreatedAt());
        dto.setUpdatedAt(marca.getUpdatedAt());
        dto.setCategorias(marca.getCategorias().stream()
                .map(CategoriaMapper::toDTO)
                .collect(Collectors.toList()));
        dto.setProveedores(marca.getProveedores().stream()
                .map(ProveedorMapper::toDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Marca toEntity(CrearMarcaDTO dto) {
        if (dto == null) return null;

        Marca marca = new Marca();
        marca.setNombre(dto.getNombre());
        marca.setCreatedAt(java.time.LocalDateTime.now());
        marca.setUpdatedAt(java.time.LocalDateTime.now());
        return marca;
    }
}
