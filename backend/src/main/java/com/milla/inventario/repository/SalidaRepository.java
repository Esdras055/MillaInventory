package com.milla.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.milla.inventario.entity.Salidas;

@Repository
public interface SalidaRepository extends JpaRepository<Salidas, Long> {

}
