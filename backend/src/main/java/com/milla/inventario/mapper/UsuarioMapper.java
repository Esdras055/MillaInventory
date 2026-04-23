package com.milla.inventario.mapper;

import com.milla.inventario.dto.usuario.CrearUsuarioDTO;
import com.milla.inventario.dto.usuario.UsuarioDTO;
import com.milla.inventario.entity.Usuario;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(Usuario user) {
        if (user == null) return null;
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setUsername(user.getUsername());
        dto.setEnabled(user.isEnabled());
        dto.setAccountNonLocked(user.isAccountNonLocked());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setLastLogin(user.getLastLogin());
        return dto;
    }

    public static Usuario toEntity(CrearUsuarioDTO dto) {
        if (dto == null) return null;
        Usuario user = new Usuario();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setCreatedAt(java.time.LocalDateTime.now());
        user.setUpdatedAt(java.time.LocalDateTime.now());
        return user;
    }
}
