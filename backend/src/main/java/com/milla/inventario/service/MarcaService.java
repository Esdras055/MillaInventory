package com.milla.inventario.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.marca.ActualizarMarcaDTO;
import com.milla.inventario.dto.marca.CrearMarcaDTO;
import com.milla.inventario.dto.marca.MarcaDTO;
import com.milla.inventario.entity.Marca;
import com.milla.inventario.mapper.MarcaMapper;
import com.milla.inventario.repository.MarcaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MarcaService implements IMarcaService {

    private final MarcaRepository marcaRepository;

    @Override
    public MarcaDTO create(CrearMarcaDTO request) {
        validateCreateRequest(request);

        marcaRepository.findByNombre(request.getNombre()).ifPresent(marca -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de la marca ya existe");
        });

        Marca marca = Objects.requireNonNull(MarcaMapper.toEntity(request));
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

        existing.setUpdatedAt(new java.util.Date());
        return MarcaMapper.toDTO(marcaRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Marca existing = marcaRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Marca no encontrada"));
        marcaRepository.delete(Objects.requireNonNull(existing));
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
