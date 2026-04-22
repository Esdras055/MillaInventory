package com.milla.inventario.mapper;

import com.milla.inventario.dto.stock.BodegaProductoDTO;
import com.milla.inventario.dto.stock.CrearBodegaProductoDTO;
import com.milla.inventario.entity.Bodega;
import com.milla.inventario.entity.BodegaProducto;
import com.milla.inventario.entity.Producto;

public class BodegaProductoMapper {

    public static BodegaProductoDTO toDTO(BodegaProducto stock) {
        if (stock == null) return null;

        BodegaProductoDTO dto = new BodegaProductoDTO();
        dto.setId(stock.getId());
        if (stock.getProducto() != null) {
            dto.setProductoId(stock.getProducto().getId());
            dto.setProductoNombre(stock.getProducto().getNombre());
        }
        if (stock.getBodega() != null) {
            dto.setBodegaId(stock.getBodega().getId());
            dto.setBodegaNombre(stock.getBodega().getNombre());
        }
        dto.setCantidad(stock.getCantidad());
        dto.setCreatedAt(stock.getCreatedAt());
        dto.setUpdatedAt(stock.getUpdatedAt());
        return dto;
    }

    public static BodegaProducto toEntity(CrearBodegaProductoDTO dto, Producto producto, Bodega bodega) {
        if (dto == null) return null;

        BodegaProducto stock = new BodegaProducto();
        stock.setProducto(producto);
        stock.setBodega(bodega);
        stock.setCantidad(dto.getCantidad());
        stock.setCreatedAt(new java.util.Date());
        stock.setUpdatedAt(new java.util.Date());
        return stock;
    }
}
