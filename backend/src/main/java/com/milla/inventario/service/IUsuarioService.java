package com.milla.inventario.service;

import java.util.List;

import com.milla.inventario.dto.usuario.ActualizarUsuarioDTO;
import com.milla.inventario.dto.usuario.CrearUsuarioDTO;
import com.milla.inventario.dto.usuario.UsuarioDTO;

public interface IUsuarioService {
    UsuarioDTO create(CrearUsuarioDTO request);
    UsuarioDTO update(Long id, ActualizarUsuarioDTO request);
    void delete(Long id);
    UsuarioDTO findById(Long id);
    List<UsuarioDTO> findAll();
}
