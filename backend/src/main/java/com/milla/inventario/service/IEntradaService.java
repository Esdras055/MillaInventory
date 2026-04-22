package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.entrada.ActualizarEntradaDTO;
import com.milla.inventario.dto.entrada.CrearEntradaDTO;
import com.milla.inventario.dto.entrada.EntradaDTO;

public interface IEntradaService {
    EntradaDTO create(CrearEntradaDTO request);
    EntradaDTO update(Long id, ActualizarEntradaDTO request);
    void delete(Long id);
    EntradaDTO findById(Long id);
    List<EntradaDTO> findAll();
    List<EntradaDTO> findByProductoId(Long productoId);
    List<EntradaDTO> findByBodegaId(Long bodegaId);
    List<EntradaDTO> findByProveedorId(Long proveedorId);
}
