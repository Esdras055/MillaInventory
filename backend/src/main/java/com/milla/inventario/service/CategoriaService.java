package com.milla.inventario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.categoria.ActualizarCategoriaDTO;
import com.milla.inventario.dto.categoria.CategoriaDTO;
import com.milla.inventario.dto.categoria.CrearCategoriaDTO;
import com.milla.inventario.entity.Categoria;
import com.milla.inventario.mapper.CategoriaMapper;
import com.milla.inventario.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService implements ICategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public CategoriaDTO create(CrearCategoriaDTO request) {
        validateCreateRequest(request);

        categoriaRepository.findByNombre(request.getNombre()).ifPresent(categoria -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de la categoria ya existe");
        });

        return CategoriaMapper.toDTO(categoriaRepository.save(CategoriaMapper.toEntity(request)));
    }

    @Override
    public CategoriaDTO update(Long id, ActualizarCategoriaDTO request) {
        validateUpdateRequest(request);

        Categoria existing = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));

        if (request.getNombre() != null && !request.getNombre().equals(existing.getNombre())) {
            categoriaRepository.findByNombre(request.getNombre()).ifPresent(categoria -> {
                if (!categoria.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de la categoria ya existe");
                }
            });
            existing.setNombre(request.getNombre());
        }

        existing.setUpdatedAt(new java.util.Date());
        return CategoriaMapper.toDTO(categoriaRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Categoria existing = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
        categoriaRepository.delete(existing);
    }

    @Override
    public CategoriaDTO findById(Long id) {
        return categoriaRepository.findById(id)
                .map(CategoriaMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
    }

    @Override
    public List<CategoriaDTO> findAll() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validateCreateRequest(CrearCategoriaDTO request) {
        if (request == null || isBlank(request.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre es requerido");
        }
    }

    private void validateUpdateRequest(ActualizarCategoriaDTO request) {
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
