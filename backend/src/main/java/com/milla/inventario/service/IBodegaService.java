package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.bodega.ActualizarBodegaDTO;
import com.milla.inventario.dto.bodega.BodegaDTO;
import com.milla.inventario.dto.bodega.CrearBodegaDTO;

public interface IBodegaService {
    BodegaDTO create(CrearBodegaDTO request);
    BodegaDTO update(Long id, ActualizarBodegaDTO request);
    void delete(Long id);
    BodegaDTO findById(Long id);
    List<BodegaDTO> findAll();
}
