package com.milla.inventario.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.entrada.ActualizarEntradaDTO;
import com.milla.inventario.dto.entrada.CrearEntradaDTO;
import com.milla.inventario.dto.entrada.EntradaDTO;
import com.milla.inventario.entity.Bodega;
import com.milla.inventario.entity.BodegaProducto;
import com.milla.inventario.entity.Entrada;
import com.milla.inventario.entity.Producto;
import com.milla.inventario.entity.Proveedor;
import com.milla.inventario.mapper.EntradaMapper;
import com.milla.inventario.repository.BodegaProductoRepository;
import com.milla.inventario.repository.BodegaRepository;
import com.milla.inventario.repository.EntradaRepository;
import com.milla.inventario.repository.ProductoRepository;
import com.milla.inventario.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EntradaService implements IEntradaService {

    private final EntradaRepository entradaRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;
    private final BodegaRepository bodegaRepository;
    private final BodegaProductoRepository bodegaProductoRepository;

    @Override
    public EntradaDTO create(CrearEntradaDTO request) {
        validateCreateRequest(request);
        Long productoId = Objects.requireNonNull(request.getProductoId());
        Long proveedorId = Objects.requireNonNull(request.getProveedorId());
        Long bodegaId = Objects.requireNonNull(request.getBodegaId());
        Integer cantidad = Objects.requireNonNull(request.getCantidad());

        Producto producto = findProductoOrThrow(productoId);
        Proveedor proveedor = findProveedorOrThrow(proveedorId);
        Bodega bodega = findBodegaOrThrow(bodegaId);

        Entrada entrada = Objects.requireNonNull(EntradaMapper.toEntity(request, producto, proveedor, bodega));
        increaseStock(producto, bodega, cantidad);

        return EntradaMapper.toDTO(entradaRepository.save(entrada));
    }

    @Override
    public EntradaDTO update(Long id, ActualizarEntradaDTO request) {
        validateUpdateRequest(request);

        Entrada existing = entradaRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrada no encontrada"));

        Producto previousProducto = existing.getProducto();
        Bodega previousBodega = existing.getBodega();
        Integer previousCantidad = existing.getCantidad();

        Producto producto = request.getProductoId() != null ? findProductoOrThrow(request.getProductoId()) : existing.getProducto();
        Proveedor proveedor = request.getProveedorId() != null ? findProveedorOrThrow(request.getProveedorId()) : existing.getProveedor();
        Bodega bodega = request.getBodegaId() != null ? findBodegaOrThrow(request.getBodegaId()) : existing.getBodega();
        Integer cantidad = request.getCantidad() != null ? request.getCantidad() : existing.getCantidad();

        decreaseStock(previousProducto, previousBodega, previousCantidad);
        increaseStock(producto, bodega, cantidad);

        existing.setProducto(producto);
        existing.setProveedor(proveedor);
        existing.setBodega(bodega);
        if (request.getFecha() != null) existing.setFecha(request.getFecha());
        if (request.getPrecioAdquisicion() != null) existing.setPrecioAdquisicion(request.getPrecioAdquisicion());
        existing.setCantidad(cantidad);
        existing.setUpdatedAt(new java.util.Date());

        return EntradaMapper.toDTO(entradaRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Entrada existing = entradaRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrada no encontrada"));

        decreaseStock(existing.getProducto(), existing.getBodega(), existing.getCantidad());
        entradaRepository.delete(Objects.requireNonNull(existing));
    }

    @Override
    public EntradaDTO findById(Long id) {
        return entradaRepository.findById(Objects.requireNonNull(id))
                .map(EntradaMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrada no encontrada"));
    }

    @Override
    public List<EntradaDTO> findAll() {
        return entradaRepository.findAll().stream()
                .map(EntradaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntradaDTO> findByProductoId(Long productoId) {
        return entradaRepository.findByProductoId(productoId).stream()
                .map(EntradaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntradaDTO> findByBodegaId(Long bodegaId) {
        return entradaRepository.findByBodegaId(bodegaId).stream()
                .map(EntradaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EntradaDTO> findByProveedorId(Long proveedorId) {
        return entradaRepository.findByProveedorId(proveedorId).stream()
                .map(EntradaMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void increaseStock(Producto producto, Bodega bodega, Integer cantidad) {
        BodegaProducto stock = bodegaProductoRepository.findByProductoIdAndBodegaId(producto.getId(), bodega.getId())
                .orElseGet(() -> {
                    BodegaProducto nuevoStock = new BodegaProducto();
                    nuevoStock.setProducto(producto);
                    nuevoStock.setBodega(bodega);
                    nuevoStock.setCantidad(0);
                    nuevoStock.setCreatedAt(new java.util.Date());
                    nuevoStock.setUpdatedAt(new java.util.Date());
                    return nuevoStock;
                });

        stock.setCantidad(stock.getCantidad() + cantidad);
        stock.setUpdatedAt(new java.util.Date());
        bodegaProductoRepository.save(stock);
    }

    private void decreaseStock(Producto producto, Bodega bodega, Integer cantidad) {
        BodegaProducto stock = bodegaProductoRepository.findByProductoIdAndBodegaId(producto.getId(), bodega.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "No existe stock para reversar la entrada"));

        int nuevaCantidad = stock.getCantidad() - cantidad;
        if (nuevaCantidad < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No hay stock suficiente para reversar la entrada");
        }

        stock.setCantidad(nuevaCantidad);
        stock.setUpdatedAt(new java.util.Date());
        bodegaProductoRepository.save(stock);
    }

    private Producto findProductoOrThrow(Long productoId) {
        return productoRepository.findById(Objects.requireNonNull(productoId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private Proveedor findProveedorOrThrow(Long proveedorId) {
        return proveedorRepository.findById(Objects.requireNonNull(proveedorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
    }

    private Bodega findBodegaOrThrow(Long bodegaId) {
        return bodegaRepository.findById(Objects.requireNonNull(bodegaId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada"));
    }

    private void validateCreateRequest(CrearEntradaDTO request) {
        if (request == null
                || request.getProductoId() == null
                || request.getProveedorId() == null
                || request.getBodegaId() == null
                || request.getFecha() == null
                || request.getPrecioAdquisicion() == null
                || request.getCantidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProductoId, proveedorId, bodegaId, fecha, precioAdquisicion y cantidad son requeridos");
        }
        validatePrecio(request.getPrecioAdquisicion());
        validateCantidad(request.getCantidad());
    }

    private void validateUpdateRequest(ActualizarEntradaDTO request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud es requerido");
        }
        if (request.getPrecioAdquisicion() != null) {
            validatePrecio(request.getPrecioAdquisicion());
        }
        if (request.getCantidad() != null) {
            validateCantidad(request.getCantidad());
        }
    }

    private void validatePrecio(BigDecimal precio) {
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precio de adquisicion debe ser mayor que cero");
        }
    }

    private void validateCantidad(Integer cantidad) {
        if (cantidad <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cantidad debe ser mayor que cero");
        }
    }
}
