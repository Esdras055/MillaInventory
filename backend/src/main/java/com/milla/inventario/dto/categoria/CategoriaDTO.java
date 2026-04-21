package com.milla.inventario.dto.categoria;

import java.util.Date;

import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    private String nombre;
    private Date createdAt;
    private Date updatedAt;
}
