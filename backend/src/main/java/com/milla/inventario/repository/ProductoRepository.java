package com.milla.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.milla.inventario.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByNombre(String nombre);

    @Modifying
    @Query(
        value = "DELETE FROM marcas_productos WHERE productoid = :id",
        nativeQuery = true
    )
    void deleteMarcasProducto(@Param("id") Long id);

    @Modifying
    @Query(
        value = "DELETE FROM bodegas_productos WHERE productoid = :id",
        nativeQuery = true
    )
    void deleteBodegasProducto(@Param("id") Long id);

    @Modifying
    @Query(
        value = "DELETE FROM entradas WHERE productoid = :id",
        nativeQuery = true
    )
    void deleteEntradasProducto(@Param("id") Long id);

    @Modifying
    @Query(
        value = "DELETE FROM salidas WHERE productoid = :id",
        nativeQuery = true
    )
    void deleteSalidasProducto(@Param("id") Long id);

}