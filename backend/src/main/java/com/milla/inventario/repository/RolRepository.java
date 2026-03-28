package com.milla.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milla.inventario.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
    
}
