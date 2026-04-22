package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.stock.ActualizarStockDTO;
import com.milla.inventario.dto.stock.BodegaProductoDTO;
import com.milla.inventario.dto.stock.CrearBodegaProductoDTO;

public interface IBodegaProductoService {
    BodegaProductoDTO create(CrearBodegaProductoDTO request);
    BodegaProductoDTO update(Long id, ActualizarStockDTO request);
    void delete(Long id);
    BodegaProductoDTO findById(Long id);
    List<BodegaProductoDTO> findAll();
    List<BodegaProductoDTO> findByProductoId(Long productoId);
    List<BodegaProductoDTO> findByBodegaId(Long bodegaId);
}
