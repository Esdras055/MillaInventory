package com.milla.inventario.mapper;

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
        return dto;
    }

    public static Marca toEntity(CrearMarcaDTO dto) {
        if (dto == null) return null;

        Marca marca = new Marca();
        marca.setNombre(dto.getNombre());
        marca.setCreatedAt(new java.util.Date());
        marca.setUpdatedAt(new java.util.Date());
        return marca;
    }
}
