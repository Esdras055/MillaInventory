package com.milla.inventario.mapper;

import com.milla.inventario.dto.ubicacion.CrearUbicacionDTO;
import com.milla.inventario.dto.ubicacion.UbicacionDTO;
import com.milla.inventario.entity.Ubicacion;

public class UbicacionMapper {

    public static UbicacionDTO toDTO(Ubicacion ubicacion) {
        if (ubicacion == null) return null;

        UbicacionDTO dto = new UbicacionDTO();
        dto.setId(ubicacion.getId());
        dto.setMunicipio(ubicacion.getMunicipio());
        dto.setCreatedAt(ubicacion.getCreatedAt());
        dto.setUpdatedAt(ubicacion.getUpdatedAt());
        return dto;
    }

    public static Ubicacion toEntity(CrearUbicacionDTO dto) {
        if (dto == null) return null;

        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setMunicipio(dto.getMunicipio());
        ubicacion.setCreatedAt(new java.util.Date());
        ubicacion.setUpdatedAt(new java.util.Date());
        return ubicacion;
    }
}
