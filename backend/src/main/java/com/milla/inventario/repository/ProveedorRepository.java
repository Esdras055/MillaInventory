package com.milla.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.milla.inventario.entity.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByNombre(String nombre);
    Optional<Proveedor> findByEmail(String email);
    Optional<Proveedor> findByTelefono(String telefono);

    @Query(value = "SELECT COUNT(*) FROM entradas WHERE proveedorid = :id", nativeQuery = true)
    long countEntradasByProveedorId(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM marcas_proveedores WHERE proveedorid = :id", nativeQuery = true)
    void deleteProveedorMarcas(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM proveedores WHERE id = :id", nativeQuery = true)
    void deleteProveedorById(@Param("id") Long id);
}
