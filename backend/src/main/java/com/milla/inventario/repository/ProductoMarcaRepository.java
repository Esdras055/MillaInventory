package com.milla.inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.milla.inventario.entity.ProductoMarca;

@Repository
public interface ProductoMarcaRepository extends JpaRepository<ProductoMarca, Long> {

}
