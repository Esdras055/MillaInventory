package com.milla.inventario.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "entradas")
@Data @AllArgsConstructor @NoArgsConstructor
public class Entrada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "productoid")
    private Producto producto;
    @ManyToOne(optional = false)
    @JoinColumn(name = "proveedorid")
    private Proveedor proveedor;
    @ManyToOne(optional = false)
    @JoinColumn(name = "bodegaid")
    private Bodega bodega;
    private LocalDate fecha;
    private BigDecimal precioAdquisicion;
    private Integer cantidad;
    private Date createdAt;
    private Date updatedAt;
}
