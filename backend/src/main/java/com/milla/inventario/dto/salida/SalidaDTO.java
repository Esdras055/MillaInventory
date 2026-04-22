package com.milla.inventario.dto.salida;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SalidaDTO {
    private Long id;
    private Long productoId;
    private Long bodegaId;
    private LocalDate fecha;
    private Integer cantidad;
}
