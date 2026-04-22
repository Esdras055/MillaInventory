package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.producto.ActualizarProductoDTO;
import com.milla.inventario.dto.producto.CrearProductoDTO;
import com.milla.inventario.dto.producto.ProductoDTO;

public interface IProductoService {
    ProductoDTO create(CrearProductoDTO request);
    ProductoDTO update(Long id, ActualizarProductoDTO request);
    void delete(Long id);
    ProductoDTO findById(Long id);
    List<ProductoDTO> findAll();
}
