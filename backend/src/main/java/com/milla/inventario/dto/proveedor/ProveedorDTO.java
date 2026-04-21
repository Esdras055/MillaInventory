package com.milla.inventario.dto.proveedor;

import java.util.Date;

import lombok.Data;

@Data
public class ProveedorDTO {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private Date createdAt;
    private Date updatedAt;
}
