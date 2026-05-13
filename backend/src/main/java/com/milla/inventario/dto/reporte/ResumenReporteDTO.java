package com.milla.inventario.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenReporteDTO {
    private Long totalProductos;
    private Long totalProveedores;
    private Long totalBodegas;
    private Long stockTotal;
    private Long unidadesEntradasMes;
    private Long unidadesSalidasMes;
}
