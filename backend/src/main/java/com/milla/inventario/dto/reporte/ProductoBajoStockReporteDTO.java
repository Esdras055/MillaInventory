package com.milla.inventario.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoBajoStockReporteDTO {
    private Long productoId;
    private String producto;
    private Integer stockActual;
    private Integer stockMinimo;
}
