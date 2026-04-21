package com.milla.inventario.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "bodegas_productos",
    uniqueConstraints = @UniqueConstraint(columnNames = {"productoid", "bodegaid"})
)
@Data @AllArgsConstructor @NoArgsConstructor
public class BodegaProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "productoid")
    private Producto producto;
    @ManyToOne(optional = false)
    @JoinColumn(name = "bodegaid")
    private Bodega bodega;
    private Integer cantidad;
    private Date createdAt;
    private Date updatedAt;
}
