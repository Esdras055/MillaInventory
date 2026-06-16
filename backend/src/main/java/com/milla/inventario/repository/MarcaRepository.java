package com.milla.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.milla.inventario.entity.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Optional<Marca> findByNombre(String nombre);

    @Modifying
    @Query(value = "DELETE FROM marcas_categorias WHERE marcaid = :id", nativeQuery = true)
    void deleteMarcasCategorias(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM marcas_proveedores WHERE marcaid = :id", nativeQuery = true)
    void deleteMarcasProveedores(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM marcas_productos WHERE marcaid = :id", nativeQuery = true)
    void deleteMarcasProductos(@Param("id") Long id);
}
