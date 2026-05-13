package com.milla.inventario.service;

import java.time.LocalDate;
import java.util.List;

import com.milla.inventario.dto.reporte.MovimientoReporteDTO;
import com.milla.inventario.dto.reporte.ProductoBajoStockReporteDTO;
import com.milla.inventario.dto.reporte.ResumenReporteDTO;
import com.milla.inventario.dto.reporte.StockReporteDTO;

public interface IReporteService {
    ResumenReporteDTO obtenerResumen();

    List<StockReporteDTO> obtenerStockPorBodega(Long bodegaId);

    List<StockReporteDTO> obtenerStockPorProducto(Long productoId);

    List<ProductoBajoStockReporteDTO> obtenerProductosBajoStock(Integer minimo);

    List<MovimientoReporteDTO> obtenerMovimientosPorProducto(Long productoId);

    List<MovimientoReporteDTO> obtenerMovimientosPorRango(LocalDate fechaInicio, LocalDate fechaFin);
}
