package com.milla.inventario.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.reporte.MovimientoReporteDTO;
import com.milla.inventario.dto.reporte.ProductoBajoStockReporteDTO;
import com.milla.inventario.dto.reporte.ResumenReporteDTO;
import com.milla.inventario.dto.reporte.StockReporteDTO;
import com.milla.inventario.entity.BodegaProducto;
import com.milla.inventario.entity.Entrada;
import com.milla.inventario.entity.Producto;
import com.milla.inventario.entity.Salidas;
import com.milla.inventario.repository.BodegaProductoRepository;
import com.milla.inventario.repository.BodegaRepository;
import com.milla.inventario.repository.EntradaRepository;
import com.milla.inventario.repository.ProductoRepository;
import com.milla.inventario.repository.ProveedorRepository;
import com.milla.inventario.repository.SalidaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReporteService implements IReporteService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final BodegaRepository bodegaRepository;
    private final BodegaProductoRepository bodegaProductoRepository;
    private final EntradaRepository entradaRepository;
    private final SalidaRepository salidaRepository;

    @Override
    public ResumenReporteDTO obtenerResumen() {
        YearMonth mesActual = YearMonth.now();
        LocalDate inicioMes = mesActual.atDay(1);
        LocalDate finMes = mesActual.atEndOfMonth();

        Long stockTotal = bodegaProductoRepository.findAll().stream()
                .mapToLong(stock -> stock.getCantidad() == null ? 0 : stock.getCantidad())
                .sum();
        Long unidadesEntradasMes = sumarCantidadesEntradas(entradaRepository.findByFechaBetween(inicioMes, finMes));
        Long unidadesSalidasMes = sumarCantidadesSalidas(salidaRepository.findByFechaBetween(inicioMes, finMes));

        return new ResumenReporteDTO(
                productoRepository.count(),
                proveedorRepository.count(),
                bodegaRepository.count(),
                stockTotal,
                unidadesEntradasMes,
                unidadesSalidasMes
        );
    }

    @Override
    public List<StockReporteDTO> obtenerStockPorBodega(Long bodegaId) {
        validarId(bodegaId, "El id de bodega es requerido");
        return bodegaProductoRepository.findByBodegaId(bodegaId).stream()
                .map(this::toStockReporte)
                .toList();
    }

    @Override
    public List<StockReporteDTO> obtenerStockPorProducto(Long productoId) {
        validarId(productoId, "El id de producto es requerido");
        return bodegaProductoRepository.findByProductoId(productoId).stream()
                .map(this::toStockReporte)
                .toList();
    }

    @Override
    public List<ProductoBajoStockReporteDTO> obtenerProductosBajoStock(Integer minimo) {
        int stockMinimo = minimo == null ? 5 : minimo;
        if (stockMinimo < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El minimo no puede ser negativo");
        }

        Map<Long, Integer> stockPorProducto = bodegaProductoRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        stock -> stock.getProducto().getId(),
                        Collectors.summingInt(stock -> stock.getCantidad() == null ? 0 : stock.getCantidad())
                ));

        return productoRepository.findAll().stream()
                .map(producto -> new ProductoBajoStockReporteDTO(
                        producto.getId(),
                        producto.getNombre(),
                        stockPorProducto.getOrDefault(producto.getId(), 0),
                        stockMinimo
                ))
                .filter(reporte -> reporte.getStockActual() <= stockMinimo)
                .toList();
    }

    @Override
    public List<MovimientoReporteDTO> obtenerMovimientosPorProducto(Long productoId) {
        validarId(productoId, "El id de producto es requerido");

        Stream<MovimientoReporteDTO> entradas = entradaRepository.findByProductoId(productoId).stream()
                .map(this::toMovimientoEntrada);
        Stream<MovimientoReporteDTO> salidas = salidaRepository.findByProductoId(productoId).stream()
                .map(this::toMovimientoSalida);

        return ordenarMovimientos(Stream.concat(entradas, salidas));
    }

    @Override
    public List<MovimientoReporteDTO> obtenerMovimientosPorRango(LocalDate fechaInicio, LocalDate fechaFin) {
        validarRangoFechas(fechaInicio, fechaFin);

        Stream<MovimientoReporteDTO> entradas = entradaRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(this::toMovimientoEntrada);
        Stream<MovimientoReporteDTO> salidas = salidaRepository.findByFechaBetween(fechaInicio, fechaFin).stream()
                .map(this::toMovimientoSalida);

        return ordenarMovimientos(Stream.concat(entradas, salidas));
    }

    private StockReporteDTO toStockReporte(BodegaProducto stock) {
        return new StockReporteDTO(
                stock.getProducto().getId(),
                stock.getProducto().getNombre(),
                stock.getBodega().getId(),
                stock.getBodega().getNombre(),
                stock.getCantidad()
        );
    }

    private MovimientoReporteDTO toMovimientoEntrada(Entrada entrada) {
        Producto producto = entrada.getProducto();
        return new MovimientoReporteDTO(
                "ENTRADA",
                entrada.getId(),
                entrada.getFecha(),
                producto.getId(),
                producto.getNombre(),
                entrada.getBodega().getId(),
                entrada.getBodega().getNombre(),
                entrada.getProveedor().getId(),
                entrada.getProveedor().getNombre(),
                entrada.getCantidad(),
                entrada.getPrecioAdquisicion()
        );
    }

    private MovimientoReporteDTO toMovimientoSalida(Salidas salida) {
        Producto producto = salida.getProducto();
        return new MovimientoReporteDTO(
                "SALIDA",
                salida.getId(),
                salida.getFecha(),
                producto.getId(),
                producto.getNombre(),
                salida.getBodega().getId(),
                salida.getBodega().getNombre(),
                null,
                null,
                salida.getCantidad(),
                null
        );
    }

    private List<MovimientoReporteDTO> ordenarMovimientos(Stream<MovimientoReporteDTO> movimientos) {
        return movimientos
                .sorted(Comparator
                        .comparing(MovimientoReporteDTO::getFecha, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(MovimientoReporteDTO::getTipo)
                        .thenComparing(MovimientoReporteDTO::getMovimientoId))
                .toList();
    }

    private Long sumarCantidadesEntradas(List<Entrada> entradas) {
        return entradas.stream()
                .mapToLong(entrada -> entrada.getCantidad() == null ? 0 : entrada.getCantidad())
                .sum();
    }

    private Long sumarCantidadesSalidas(List<Salidas> salidas) {
        return salidas.stream()
                .mapToLong(salida -> salida.getCantidad() == null ? 0 : salida.getCantidad())
                .sum();
    }

    private void validarId(Long id, String mensaje) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje);
        }
    }

    private void validarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInicio y fechaFin son requeridas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fechaInicio no puede ser mayor que fechaFin");
        }
    }
}
