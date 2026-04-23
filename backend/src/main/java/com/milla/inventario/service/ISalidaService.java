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

    List<SalidaDTO> listarPorProducto(Long productoId);

    List<SalidaDTO> listarPorBodega(Long bodegaId);

    void eliminar(Long id);
}
