package com.milla.inventario.dto.reporte;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoReporteDTO {
    private String tipo;
    private Long movimientoId;
    private LocalDate fecha;
    private Long productoId;
    private String producto;
    private Long bodegaId;
    private String bodega;
    private Long proveedorId;
    private String proveedor;
    private Integer cantidad;
    private BigDecimal precioAdquisicion;
}
