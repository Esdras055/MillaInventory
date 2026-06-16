package com.milla.inventario.dto.marca;

import java.time.LocalDateTime;
import java.util.List;

import com.milla.inventario.dto.categoria.CategoriaDTO;
import com.milla.inventario.dto.proveedor.ProveedorDTO;

import lombok.Data;

@Data
public class MarcaDTO {
    private Long id;
    private String nombre;
    private List<CategoriaDTO> categorias;
    private List<ProveedorDTO> proveedores;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
