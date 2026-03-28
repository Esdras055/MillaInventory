package com.milla.inventario.mapper;

import com.milla.inventario.dto.bodega.BodegaDTO;
import com.milla.inventario.entity.Bodega;

public class BodegaMapper {

    public static BodegaDTO toDTO(Bodega bodega) {
        if (bodega == null) return null;

        BodegaDTO dto = new BodegaDTO();
        dto.setId(bodega.getId());
        dto.setNombre(bodega.getNombre());
        dto.setCreatedAt(bodega.getCreatedAt());
        dto.setUpdatedAt(bodega.getUpdatedAt());

        if (bodega.getUbicacion() != null) {
            dto.setUbicacionId(bodega.getUbicacion().getId());
            dto.setMunicipio(bodega.getUbicacion().getMunicipio());
        }

        return dto;
    }
}
