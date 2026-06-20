package com.milla.inventario.service;

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

import com.milla.inventario.dto.marca.ActualizarMarcaDTO;
import com.milla.inventario.dto.marca.CrearMarcaDTO;
import com.milla.inventario.dto.marca.MarcaDTO;
import com.milla.inventario.entity.Categoria;
import com.milla.inventario.entity.Marca;
import com.milla.inventario.entity.Proveedor;
import com.milla.inventario.mapper.MarcaMapper;
import com.milla.inventario.repository.CategoriaRepository;
import com.milla.inventario.repository.MarcaRepository;
import com.milla.inventario.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MarcaService implements IMarcaService {

    private final MarcaRepository marcaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    @Override
    public MarcaDTO create(CrearMarcaDTO request) {
        validateCreateRequest(request);

        marcaRepository.findByNombre(request.getNombre()).ifPresent(marca -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de la marca ya existe");
        });

        Marca marca = Objects.requireNonNull(MarcaMapper.toEntity(request));
        marca.setCategorias(findCategoriasOrThrow(request.getCategoriaIds()));
        marca.setProveedores(findProveedoresOrThrow(request.getProveedorIds()));

        return MarcaMapper.toDTO(marcaRepository.save(marca));
    }

    @Override
    public MarcaDTO update(Long id, ActualizarMarcaDTO request) {
        validateUpdateRequest(request);

        Marca existing = marcaRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca no encontrada"));

        if (request.getNombre() != null && !request.getNombre().equals(existing.getNombre())) {
            marcaRepository.findByNombre(request.getNombre()).ifPresent(marca -> {
                if (!marca.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de la marca ya existe");
                }
            });
            existing.setNombre(request.getNombre());
        }

        if (request.getCategoriaIds() != null) {
            existing.setCategorias(findCategoriasOrThrow(request.getCategoriaIds()));
        }

        if (request.getProveedorIds() != null) {
            existing.setProveedores(findProveedoresOrThrow(request.getProveedorIds()));
        }

        existing.setUpdatedAt(java.time.LocalDateTime.now());
        return MarcaMapper.toDTO(marcaRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!marcaRepository.existsById(Objects.requireNonNull(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca no encontrada");
        }

        marcaRepository.deleteMarcasCategorias(id);
        marcaRepository.deleteMarcasProveedores(id);
        marcaRepository.deleteMarcasProductos(id);
        marcaRepository.deleteMarcaById(id);
    }

    @Override
    public MarcaDTO findById(Long id) {
        return marcaRepository.findById(Objects.requireNonNull(id))
                .map(MarcaMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca no encontrada"));
    }

    @Override
    public List<MarcaDTO> findAll() {
        return marcaRepository.findAll().stream()
                .map(MarcaMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Set<Categoria> findCategoriasOrThrow(List<Long> categoriaIds) {
        if (categoriaIds == null || categoriaIds.isEmpty()) {
            return Collections.emptySet();
        }
        List<Categoria> categorias = categoriaRepository.findAllById(categoriaIds);
        if (categorias.size() != new HashSet<>(categoriaIds).size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Una o mas categorias no fueron encontradas");
        }
        return new HashSet<>(categorias);
    }

    private Set<Proveedor> findProveedoresOrThrow(List<Long> proveedorIds) {
        if (proveedorIds == null || proveedorIds.isEmpty()) {
            return Collections.emptySet();
        }
        List<Proveedor> proveedores = proveedorRepository.findAllById(proveedorIds);
        if (proveedores.size() != new HashSet<>(proveedorIds).size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Uno o mas proveedores no fueron encontrados");
        }
        return new HashSet<>(proveedores);
    }

    private void validateCreateRequest(CrearMarcaDTO request) {
        if (request == null || isBlank(request.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre es requerido");
        }
    }

    private void validateUpdateRequest(ActualizarMarcaDTO request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud es requerido");
        }
        if (request.getNombre() != null && request.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre no puede estar vacio");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
