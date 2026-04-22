package com.milla.inventario.mapper;

import com.milla.inventario.dto.rol.ActualizarRolDTO;
import com.milla.inventario.dto.rol.CrearRolDTO;
import com.milla.inventario.dto.rol.RolDTO;
import com.milla.inventario.entity.Rol;

public class RolMapper {

    public static RolDTO toDTO(Rol rol) {
        if (rol == null) {
            return null;
        }

        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        return dto;
    }

    public static Rol toEntity(CrearRolDTO dto) {
        if (dto == null) {
            return null;
        }

        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        return rol;
    }

    public static void actualizarEntity(Rol rol, ActualizarRolDTO dto) {
        if (rol == null || dto == null) {
            return;
        }

        rol.setNombre(dto.getNombre());
    }
}
