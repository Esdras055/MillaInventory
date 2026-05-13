package com.milla.inventario.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.milla.inventario.dto.reporte.MovimientoReporteDTO;
import com.milla.inventario.dto.reporte.ProductoBajoStockReporteDTO;
import com.milla.inventario.dto.reporte.ResumenReporteDTO;
import com.milla.inventario.dto.reporte.StockReporteDTO;
import com.milla.inventario.service.IReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Reportes JSON para dashboard e integracion con frontend")
public class ReporteController {

    private final IReporteService reporteService;

    @GetMapping("/resumen")
    @Operation(summary = "Resumen general", description = "Obtiene totales generales y unidades movidas en el mes actual.")
    public ResponseEntity<ResumenReporteDTO> obtenerResumen() {
        return ResponseEntity.ok(reporteService.obtenerResumen());
    }

    @GetMapping("/stock/bodega/{bodegaId}")
    @Operation(summary = "Stock por bodega", description = "Obtiene el stock actual de todos los productos en una bodega.")
    public ResponseEntity<List<StockReporteDTO>> obtenerStockPorBodega(@PathVariable Long bodegaId) {
        return ResponseEntity.ok(reporteService.obtenerStockPorBodega(bodegaId));
    }

    @GetMapping("/stock/producto/{productoId}")
    @Operation(summary = "Stock por producto", description = "Obtiene el stock actual de un producto en todas las bodegas.")
    public ResponseEntity<List<StockReporteDTO>> obtenerStockPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(reporteService.obtenerStockPorProducto(productoId));
    }

    @GetMapping("/productos-bajo-stock")
    @Operation(summary = "Productos con bajo stock", description = "Lista productos cuyo stock total es menor o igual al minimo indicado.")
    public ResponseEntity<List<ProductoBajoStockReporteDTO>> obtenerProductosBajoStock(
            @RequestParam(defaultValue = "5") Integer minimo) {
        return ResponseEntity.ok(reporteService.obtenerProductosBajoStock(minimo));
    }

    @GetMapping("/movimientos/producto/{productoId}")
    @Operation(summary = "Movimientos por producto", description = "Obtiene entradas y salidas registradas para un producto.")
    public ResponseEntity<List<MovimientoReporteDTO>> obtenerMovimientosPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(reporteService.obtenerMovimientosPorProducto(productoId));
    }

    @GetMapping("/movimientos")
    @Operation(summary = "Movimientos por rango", description = "Obtiene entradas y salidas registradas entre dos fechas.")
    public ResponseEntity<List<MovimientoReporteDTO>> obtenerMovimientosPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(reporteService.obtenerMovimientosPorRango(fechaInicio, fechaFin));
    }
}
