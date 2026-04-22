package com.milla.inventario.mapper;

import com.milla.inventario.dto.salida.ActualizarSalidaDTO;
import com.milla.inventario.dto.salida.CrearSalidaDTO;
import com.milla.inventario.dto.salida.SalidaDTO;
import com.milla.inventario.entity.Salidas;

public class SalidaMapper {

    public static SalidaDTO toDTO(Salidas salida) {
        if (salida == null) {
            return null;
        }

        SalidaDTO dto = new SalidaDTO();
        dto.setId(salida.getId());
        dto.setProductoId(salida.getProductoId());
        dto.setBodegaId(salida.getBodegaId());
        dto.setFecha(salida.getFecha());
        dto.setCantidad(salida.getCantidad());
        return dto;
    }

    public static Salidas toEntity(CrearSalidaDTO dto) {
        if (dto == null) {
            return null;
        }

        Salidas salida = new Salidas();
        salida.setProductoId(dto.getProductoId());
        salida.setBodegaId(dto.getBodegaId());
        salida.setFecha(dto.getFecha());
        salida.setCantidad(dto.getCantidad());
        return salida;
    }

    public static void actualizarEntity(Salidas salida, ActualizarSalidaDTO dto) {
        if (salida == null || dto == null) {
            return;
        }

        salida.setProductoId(dto.getProductoId());
        salida.setBodegaId(dto.getBodegaId());
        salida.setFecha(dto.getFecha());
        salida.setCantidad(dto.getCantidad());
    }
}
