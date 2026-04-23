package com.milla.inventario.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.producto.ActualizarProductoDTO;
import com.milla.inventario.dto.producto.CrearProductoDTO;
import com.milla.inventario.dto.producto.ProductoDTO;
import com.milla.inventario.entity.Categoria;
import com.milla.inventario.entity.Marca;
import com.milla.inventario.entity.Producto;
import com.milla.inventario.mapper.ProductoMapper;
import com.milla.inventario.repository.CategoriaRepository;
import com.milla.inventario.repository.MarcaRepository;
import com.milla.inventario.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MarcaRepository marcaRepository;

    @Override
    public ProductoDTO create(CrearProductoDTO request) {
        validateCreateRequest(request);
        Long categoriaId = Objects.requireNonNull(request.getCategoriaId());

        productoRepository.findByNombre(request.getNombre()).ifPresent(producto -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre del producto ya existe");
        });

        Categoria categoria = findCategoriaOrThrow(categoriaId);
        Set<Marca> marcas = findMarcasOrThrow(request.getMarcaIds());

        Producto producto = Objects.requireNonNull(ProductoMapper.toEntity(request, categoria));
        producto.setMarcas(marcas);

        return ProductoMapper.toDTO(productoRepository.save(producto));
    }

    @Override
    public ProductoDTO update(Long id, ActualizarProductoDTO request) {
        validateUpdateRequest(request);

        Producto existing = productoRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (request.getNombre() != null && !request.getNombre().equals(existing.getNombre())) {
            productoRepository.findByNombre(request.getNombre()).ifPresent(producto -> {
                if (!producto.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre del producto ya existe");
                }
            });
            existing.setNombre(request.getNombre());
        }

        if (request.getCategoriaId() != null) {
            existing.setCategoria(findCategoriaOrThrow(request.getCategoriaId()));
        }

        if (request.getPrecio() != null) {
            existing.setPrecio(request.getPrecio());
        }

        if (request.getMarcaIds() != null) {
            existing.setMarcas(findMarcasOrThrow(request.getMarcaIds()));
        }

        existing.setUpdatedAt(java.time.LocalDateTime.now());
        return ProductoMapper.toDTO(productoRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Producto existing = productoRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        productoRepository.delete(Objects.requireNonNull(existing));
    }

    @Override
    public ProductoDTO findById(Long id) {
        return productoRepository.findById(Objects.requireNonNull(id))
                .map(ProductoMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    @Override
    public List<ProductoDTO> findAll() {
        return productoRepository.findAll().stream()
                .map(ProductoMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Categoria findCategoriaOrThrow(Long categoriaId) {
        return categoriaRepository.findById(Objects.requireNonNull(categoriaId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
    }

    private Set<Marca> findMarcasOrThrow(List<Long> marcaIds) {
        if (marcaIds == null || marcaIds.isEmpty()) {
            return Collections.emptySet();
        }

        List<Marca> marcas = marcaRepository.findAllById(marcaIds);
        if (marcas.size() != new HashSet<>(marcaIds).size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Una o mas marcas no fueron encontradas");
        }

        return new HashSet<>(marcas);
    }

    private void validateCreateRequest(CrearProductoDTO request) {
        if (request == null
                || isBlank(request.getNombre())
                || request.getCategoriaId() == null
                || request.getPrecio() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre, categoriaId y precio son requeridos");
        }
        validatePrecio(request.getPrecio());
    }

    private void validateUpdateRequest(ActualizarProductoDTO request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud es requerido");
        }
        if (request.getNombre() != null && request.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre no puede estar vacio");
        }
        if (request.getPrecio() != null) {
            validatePrecio(request.getPrecio());
        }
    }

    private void validatePrecio(BigDecimal precio) {
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Precio debe ser mayor que cero");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
