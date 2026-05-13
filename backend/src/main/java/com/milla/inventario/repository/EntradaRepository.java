package com.milla.inventario.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.milla.inventario.entity.Entrada;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    List<Entrada> findByProductoId(Long productoId);
    List<Entrada> findByBodegaId(Long bodegaId);
    List<Entrada> findByProveedorId(Long proveedorId);
    List<Entrada> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}
