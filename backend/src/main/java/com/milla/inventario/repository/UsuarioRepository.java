package com.milla.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.milla.inventario.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    @Query("""
    select r.name
    from Usuario u
    join UsuarioRol ur on u.id = ur.userId
    join Rol r on ur.roleId = r.id
    where u.username = :username
        """)
    List<String> findRolesByUsername(String username);
}
