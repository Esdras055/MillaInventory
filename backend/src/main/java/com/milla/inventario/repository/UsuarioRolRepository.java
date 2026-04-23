package com.milla.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milla.inventario.entity.UsuarioRol;
import com.milla.inventario.entity.UsuarioRolId;
 
public interface UsuarioRolRepository  extends JpaRepository<UsuarioRol, UsuarioRolId> {
    List<UsuarioRol> findByUserId(Long userId);

    Optional<UsuarioRol> findByUserIdAndRoleId(Long userId, Long roleId);
}
