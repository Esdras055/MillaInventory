package com.milla.inventario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milla.inventario.entity.BodegaProducto;

public interface BodegaProductoRepository extends JpaRepository<BodegaProducto, Long> {
    Optional<BodegaProducto> findByProductoIdAndBodegaId(Long productoId, Long bodegaId);
    List<BodegaProducto> findByProductoId(Long productoId);
    List<BodegaProducto> findByBodegaId(Long bodegaId);
}
