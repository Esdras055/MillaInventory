package com.milla.inventario.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milla.inventario.entity.Bodega;

public interface BodegaRepository extends JpaRepository<Bodega, Long> {
    Optional<Bodega> findByNombre(String nombre);
}
