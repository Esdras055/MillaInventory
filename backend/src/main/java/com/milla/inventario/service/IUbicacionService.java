package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.ubicacion.ActualizarUbicacionDTO;
import com.milla.inventario.dto.ubicacion.CrearUbicacionDTO;
import com.milla.inventario.dto.ubicacion.UbicacionDTO;

public interface IUbicacionService {
    UbicacionDTO create(CrearUbicacionDTO request);
    UbicacionDTO update(Long id, ActualizarUbicacionDTO request);
    void delete(Long id);
    UbicacionDTO findById(Long id);
    List<UbicacionDTO> findAll();
}
