package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.categoria.ActualizarCategoriaDTO;
import com.milla.inventario.dto.categoria.CategoriaDTO;
import com.milla.inventario.dto.categoria.CrearCategoriaDTO;

public interface ICategoriaService {
    CategoriaDTO create(CrearCategoriaDTO request);
    CategoriaDTO update(Long id, ActualizarCategoriaDTO request);
    void delete(Long id);
    CategoriaDTO findById(Long id);
    List<CategoriaDTO> findAll();
}
