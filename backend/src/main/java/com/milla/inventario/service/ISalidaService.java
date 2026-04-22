package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.salida.ActualizarSalidaDTO;
import com.milla.inventario.dto.salida.CrearSalidaDTO;
import com.milla.inventario.dto.salida.SalidaDTO;

public interface ISalidaService {

    SalidaDTO crear(CrearSalidaDTO dto);

    SalidaDTO actualizar(Long id, ActualizarSalidaDTO dto);

    SalidaDTO obtenerPorId(Long id);

    List<SalidaDTO> listar();

    void eliminar(Long id);
}
