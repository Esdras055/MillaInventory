package com.milla.inventario.mapper;

import com.milla.inventario.dto.salida.ActualizarSalidaDTO;
import com.milla.inventario.dto.salida.CrearSalidaDTO;
import com.milla.inventario.dto.salida.SalidaDTO;
import com.milla.inventario.entity.Bodega;
import com.milla.inventario.entity.Producto;
import com.milla.inventario.entity.Salidas;

public class SalidaMapper {

    public static SalidaDTO toDTO(Salidas salida) {
        if (salida == null) {
            return null;
        }

        SalidaDTO dto = new SalidaDTO();
        dto.setId(salida.getId());
        if (salida.getProducto() != null) {
            dto.setProductoId(salida.getProducto().getId());
            dto.setProductoNombre(salida.getProducto().getNombre());
        }
        if (salida.getBodega() != null) {
            dto.setBodegaId(salida.getBodega().getId());
            dto.setBodegaNombre(salida.getBodega().getNombre());
        }
        dto.setFecha(salida.getFecha());
        dto.setCantidad(salida.getCantidad());
        dto.setCreatedAt(salida.getCreatedAt());
        dto.setUpdatedAt(salida.getUpdatedAt());
        return dto;
    }

    public static Salidas toEntity(CrearSalidaDTO dto, Producto producto, Bodega bodega) {
        if (dto == null) {
            return null;
        }

        Salidas salida = new Salidas();
        salida.setProducto(producto);
        salida.setBodega(bodega);
        salida.setFecha(dto.getFecha());
        salida.setCantidad(dto.getCantidad());
        salida.setCreatedAt(new java.util.Date());
        salida.setUpdatedAt(new java.util.Date());
        return salida;
    }
}
