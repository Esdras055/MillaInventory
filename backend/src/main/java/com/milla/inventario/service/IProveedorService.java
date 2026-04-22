package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.proveedor.ActualizarProveedorDTO;
import com.milla.inventario.dto.proveedor.CrearProveedorDTO;
import com.milla.inventario.dto.proveedor.ProveedorDTO;

public interface IProveedorService {
    ProveedorDTO create(CrearProveedorDTO request);
    ProveedorDTO update(Long id, ActualizarProveedorDTO request);
    void delete(Long id);
    ProveedorDTO findById(Long id);
    List<ProveedorDTO> findAll();
}
