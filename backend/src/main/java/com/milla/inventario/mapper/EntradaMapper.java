package com.milla.inventario.mapper;

import com.milla.inventario.dto.entrada.CrearEntradaDTO;
import com.milla.inventario.dto.entrada.EntradaDTO;
import com.milla.inventario.entity.Bodega;
import com.milla.inventario.entity.Entrada;
import com.milla.inventario.entity.Producto;
import com.milla.inventario.entity.Proveedor;

public class EntradaMapper {

    public static EntradaDTO toDTO(Entrada entrada) {
        if (entrada == null) return null;

        EntradaDTO dto = new EntradaDTO();
        dto.setId(entrada.getId());
        if (entrada.getProducto() != null) {
            dto.setProductoId(entrada.getProducto().getId());
            dto.setProductoNombre(entrada.getProducto().getNombre());
        }
        if (entrada.getProveedor() != null) {
            dto.setProveedorId(entrada.getProveedor().getId());
            dto.setProveedorNombre(entrada.getProveedor().getNombre());
        }
        if (entrada.getBodega() != null) {
            dto.setBodegaId(entrada.getBodega().getId());
            dto.setBodegaNombre(entrada.getBodega().getNombre());
        }
        dto.setFecha(entrada.getFecha());
        dto.setPrecioAdquisicion(entrada.getPrecioAdquisicion());
        dto.setCantidad(entrada.getCantidad());
        dto.setCreatedAt(entrada.getCreatedAt());
        dto.setUpdatedAt(entrada.getUpdatedAt());
        return dto;
    }

    public static Entrada toEntity(CrearEntradaDTO dto, Producto producto, Proveedor proveedor, Bodega bodega) {
        if (dto == null) return null;

        Entrada entrada = new Entrada();
        entrada.setProducto(producto);
        entrada.setProveedor(proveedor);
        entrada.setBodega(bodega);
        entrada.setFecha(dto.getFecha());
        entrada.setPrecioAdquisicion(dto.getPrecioAdquisicion());
        entrada.setCantidad(dto.getCantidad());
        entrada.setCreatedAt(new java.util.Date());
        entrada.setUpdatedAt(new java.util.Date());
        return entrada;
    }
}
