package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.marca.ActualizarMarcaDTO;
import com.milla.inventario.dto.marca.CrearMarcaDTO;
import com.milla.inventario.dto.marca.MarcaDTO;

public interface IMarcaService {
    MarcaDTO create(CrearMarcaDTO request);
    MarcaDTO update(Long id, ActualizarMarcaDTO request);
    void delete(Long id);
    MarcaDTO findById(Long id);
    List<MarcaDTO> findAll();
}
