package com.milla.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milla.inventario.entity.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByNombre(String nombre);
    Optional<Proveedor> findByEmail(String email);
    Optional<Proveedor> findByTelefono(String telefono);
}
