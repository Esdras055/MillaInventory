package com.milla.inventario.mapper;

import com.milla.inventario.dto.proveedor.CrearProveedorDTO;
import com.milla.inventario.dto.proveedor.ProveedorDTO;
import com.milla.inventario.entity.Proveedor;

public class ProveedorMapper {

    public static ProveedorDTO toDTO(Proveedor proveedor) {
        if (proveedor == null) return null;

        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setNombre(proveedor.getNombre());
        dto.setEmail(proveedor.getEmail());
        dto.setTelefono(proveedor.getTelefono());
        dto.setCreatedAt(proveedor.getCreatedAt());
        dto.setUpdatedAt(proveedor.getUpdatedAt());
        return dto;
    }

    public static Proveedor toEntity(CrearProveedorDTO dto) {
        if (dto == null) return null;

        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(dto.getNombre());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCreatedAt(new java.util.Date());
        proveedor.setUpdatedAt(new java.util.Date());
        return proveedor;
    }
}
