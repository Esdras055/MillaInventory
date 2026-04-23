package com.milla.inventario.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.salida.ActualizarSalidaDTO;
import com.milla.inventario.dto.salida.CrearSalidaDTO;
import com.milla.inventario.dto.salida.SalidaDTO;
import com.milla.inventario.entity.Bodega;
import com.milla.inventario.entity.BodegaProducto;
import com.milla.inventario.entity.Producto;
import com.milla.inventario.entity.Salidas;
import com.milla.inventario.mapper.SalidaMapper;
import com.milla.inventario.repository.BodegaProductoRepository;
import com.milla.inventario.repository.BodegaRepository;
import com.milla.inventario.repository.ProductoRepository;
import com.milla.inventario.repository.SalidaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SalidaService implements ISalidaService {

    private final SalidaRepository salidaRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;
    private final BodegaProductoRepository bodegaProductoRepository;

    @Override
    public SalidaDTO crear(CrearSalidaDTO dto) {
        validateCreateRequest(dto);
        Long productoId = Objects.requireNonNull(dto.getProductoId());
        Long bodegaId = Objects.requireNonNull(dto.getBodegaId());
        Integer cantidad = Objects.requireNonNull(dto.getCantidad());

        Producto producto = findProductoOrThrow(productoId);
        Bodega bodega = findBodegaOrThrow(bodegaId);

        decreaseStock(producto, bodega, cantidad);

        Salidas salida = Objects.requireNonNull(SalidaMapper.toEntity(dto, producto, bodega));
        return SalidaMapper.toDTO(salidaRepository.save(salida));
    }

    @Override
    public SalidaDTO actualizar(Long id, ActualizarSalidaDTO dto) {
        validateUpdateRequest(dto);

        Salidas existing = salidaRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Salida no encontrada"));

        Producto previousProducto = existing.getProducto();
        Bodega previousBodega = existing.getBodega();
        Integer previousCantidad = existing.getCantidad();

        Producto producto = dto.getProductoId() != null ? findProductoOrThrow(dto.getProductoId()) : existing.getProducto();
        Bodega bodega = dto.getBodegaId() != null ? findBodegaOrThrow(dto.getBodegaId()) : existing.getBodega();
        Integer cantidad = dto.getCantidad() != null ? dto.getCantidad() : existing.getCantidad();

        increaseStock(previousProducto, previousBodega, previousCantidad);
        decreaseStock(producto, bodega, cantidad);

        existing.setProducto(producto);
        existing.setBodega(bodega);
        if (dto.getFecha() != null) existing.setFecha(dto.getFecha());
        existing.setCantidad(cantidad);
        existing.setUpdatedAt(java.time.LocalDateTime.now());

        return SalidaMapper.toDTO(salidaRepository.save(existing));
    }

    @Override
    public SalidaDTO obtenerPorId(Long id) {
        return salidaRepository.findById(Objects.requireNonNull(id))
                .map(SalidaMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Salida no encontrada"));
    }

    @Override
    public List<SalidaDTO> listar() {
        return salidaRepository.findAll().stream()
                .map(SalidaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalidaDTO> listarPorProducto(Long productoId) {
        return salidaRepository.findByProductoId(productoId).stream()
                .map(SalidaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SalidaDTO> listarPorBodega(Long bodegaId) {
        return salidaRepository.findByBodegaId(bodegaId).stream()
                .map(SalidaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        Salidas existing = salidaRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Salida no encontrada"));

        increaseStock(existing.getProducto(), existing.getBodega(), existing.getCantidad());
        salidaRepository.delete(Objects.requireNonNull(existing));
    }

    private void decreaseStock(Producto producto, Bodega bodega, Integer cantidad) {
        BodegaProducto stock = bodegaProductoRepository.findByProductoIdAndBodegaId(producto.getId(), bodega.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "No existe stock para ese producto en esa bodega"));

        int nuevaCantidad = stock.getCantidad() - cantidad;
        if (nuevaCantidad < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente para registrar la salida");
        }

        stock.setCantidad(nuevaCantidad);
        stock.setUpdatedAt(java.time.LocalDateTime.now());
        bodegaProductoRepository.save(stock);
    }

    private void increaseStock(Producto producto, Bodega bodega, Integer cantidad) {
        BodegaProducto stock = bodegaProductoRepository.findByProductoIdAndBodegaId(producto.getId(), bodega.getId())
                .orElseGet(() -> {
                    BodegaProducto nuevoStock = new BodegaProducto();
                    nuevoStock.setProducto(producto);
                    nuevoStock.setBodega(bodega);
                    nuevoStock.setCantidad(0);
                    nuevoStock.setCreatedAt(java.time.LocalDateTime.now());
                    nuevoStock.setUpdatedAt(java.time.LocalDateTime.now());
                    return nuevoStock;
                });

        stock.setCantidad(stock.getCantidad() + cantidad);
        stock.setUpdatedAt(java.time.LocalDateTime.now());
        bodegaProductoRepository.save(stock);
    }

    private Producto findProductoOrThrow(Long productoId) {
        return productoRepository.findById(Objects.requireNonNull(productoId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private Bodega findBodegaOrThrow(Long bodegaId) {
        return bodegaRepository.findById(Objects.requireNonNull(bodegaId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada"));
    }

    private void validateCreateRequest(CrearSalidaDTO dto) {
        if (dto == null
                || dto.getProductoId() == null
                || dto.getBodegaId() == null
                || dto.getFecha() == null
                || dto.getCantidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProductoId, bodegaId, fecha y cantidad son requeridos");
        }
        validateCantidad(dto.getCantidad());
    }

    private void validateUpdateRequest(ActualizarSalidaDTO dto) {
        if (dto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud es requerido");
        }
        if (dto.getCantidad() != null) {
            validateCantidad(dto.getCantidad());
        }
    }

    private void validateCantidad(Integer cantidad) {
        if (cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cantidad debe ser mayor que cero");
        }
    }
}
