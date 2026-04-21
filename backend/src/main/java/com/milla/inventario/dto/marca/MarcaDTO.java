package com.milla.inventario.dto.marca;

import java.util.Date;

import lombok.Data;

@Data
public class MarcaDTO {
    private Long id;
    private String nombre;
    private Date createdAt;
    private Date updatedAt;
}
