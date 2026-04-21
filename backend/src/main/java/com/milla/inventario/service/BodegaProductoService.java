package com.milla.inventario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.stock.ActualizarStockDTO;
import com.milla.inventario.dto.stock.BodegaProductoDTO;
import com.milla.inventario.dto.stock.CrearBodegaProductoDTO;
import com.milla.inventario.entity.Bodega;
import com.milla.inventario.entity.BodegaProducto;
import com.milla.inventario.entity.Producto;
import com.milla.inventario.mapper.BodegaProductoMapper;
import com.milla.inventario.repository.BodegaProductoRepository;
import com.milla.inventario.repository.BodegaRepository;
import com.milla.inventario.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BodegaProductoService implements IBodegaProductoService {

    private final BodegaProductoRepository bodegaProductoRepository;
    private final ProductoRepository productoRepository;
    private final BodegaRepository bodegaRepository;

    @Override
    public BodegaProductoDTO create(CrearBodegaProductoDTO request) {
        validateCreateRequest(request);

        bodegaProductoRepository.findByProductoIdAndBodegaId(request.getProductoId(), request.getBodegaId())
                .ifPresent(stock -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe stock para ese producto en esa bodega");
                });

        Producto producto = findProductoOrThrow(request.getProductoId());
        Bodega bodega = findBodegaOrThrow(request.getBodegaId());

        return BodegaProductoMapper.toDTO(
                bodegaProductoRepository.save(BodegaProductoMapper.toEntity(request, producto, bodega)));
    }

    @Override
    public BodegaProductoDTO update(Long id, ActualizarStockDTO request) {
        validateUpdateRequest(request);

        BodegaProducto existing = bodegaProductoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock no encontrado"));

        existing.setCantidad(request.getCantidad());
        existing.setUpdatedAt(new java.util.Date());

        return BodegaProductoMapper.toDTO(bodegaProductoRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        BodegaProducto existing = bodegaProductoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock no encontrado"));
        bodegaProductoRepository.delete(existing);
    }

    @Override
    public BodegaProductoDTO findById(Long id) {
        return bodegaProductoRepository.findById(id)
                .map(BodegaProductoMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock no encontrado"));
    }

    @Override
    public List<BodegaProductoDTO> findAll() {
        return bodegaProductoRepository.findAll().stream()
                .map(BodegaProductoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BodegaProductoDTO> findByProductoId(Long productoId) {
        return bodegaProductoRepository.findByProductoId(productoId).stream()
                .map(BodegaProductoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BodegaProductoDTO> findByBodegaId(Long bodegaId) {
        return bodegaProductoRepository.findByBodegaId(bodegaId).stream()
                .map(BodegaProductoMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Producto findProductoOrThrow(Long productoId) {
        return productoRepository.findById(productoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    private Bodega findBodegaOrThrow(Long bodegaId) {
        return bodegaRepository.findById(bodegaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada"));
    }

    private void validateCreateRequest(CrearBodegaProductoDTO request) {
        if (request == null
                || request.getProductoId() == null
                || request.getBodegaId() == null
                || request.getCantidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProductoId, bodegaId y cantidad son requeridos");
        }
        validateCantidad(request.getCantidad());
    }

    private void validateUpdateRequest(ActualizarStockDTO request) {
        if (request == null || request.getCantidad() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cantidad es requerida");
        }
        validateCantidad(request.getCantidad());
    }

    private void validateCantidad(Integer cantidad) {
        if (cantidad < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cantidad no puede ser negativa");
        }
    }
}
