package com.milla.inventario.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "marcas")
@Data @AllArgsConstructor @NoArgsConstructor
public class Marca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
        name = "marcas_categorias",
        joinColumns = @JoinColumn(name = "marcaid"),
        inverseJoinColumns = @JoinColumn(name = "categoriaid")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Categoria> categorias = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "marcas_proveedores",
        joinColumns = @JoinColumn(name = "marcaid"),
        inverseJoinColumns = @JoinColumn(name = "proveedorid")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Proveedor> proveedores = new HashSet<>();
}
