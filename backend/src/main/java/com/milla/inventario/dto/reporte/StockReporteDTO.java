package com.milla.inventario.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockReporteDTO {
    private Long productoId;
    private String producto;
    private Long bodegaId;
    private String bodega;
    private Integer stockActual;
}
