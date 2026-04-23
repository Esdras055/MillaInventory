package com.milla.inventario.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.bodega.ActualizarBodegaDTO;
import com.milla.inventario.dto.bodega.BodegaDTO;
import com.milla.inventario.dto.bodega.CrearBodegaDTO;
import com.milla.inventario.entity.Bodega;
import com.milla.inventario.entity.Ubicacion;
import com.milla.inventario.mapper.BodegaMapper;
import com.milla.inventario.repository.BodegaRepository;
import com.milla.inventario.repository.UbicacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BodegaService implements IBodegaService {

    private final BodegaRepository bodegaRepository;
    private final UbicacionRepository ubicacionRepository;

    @Override
    public BodegaDTO create(CrearBodegaDTO request) {
        validateCreateRequest(request);
        Long ubicacionId = Objects.requireNonNull(request.getUbicacionId());

        bodegaRepository.findByNombre(request.getNombre()).ifPresent(bodega -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de la bodega ya existe");
        });

        Ubicacion ubicacion = findUbicacionOrThrow(ubicacionId);

        Bodega bodega = new Bodega();
        bodega.setNombre(request.getNombre());
        bodega.setUbicacion(ubicacion);
        bodega.setCreatedAt(java.time.LocalDateTime.now());
        bodega.setUpdatedAt(java.time.LocalDateTime.now());

        return BodegaMapper.toDTO(bodegaRepository.save(bodega));
    }

    @Override
    public BodegaDTO update(Long id, ActualizarBodegaDTO request) {
        validateUpdateRequest(request);

        Bodega existing = bodegaRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada"));

        if (request.getNombre() != null && !request.getNombre().equals(existing.getNombre())) {
            bodegaRepository.findByNombre(request.getNombre()).ifPresent(bodega -> {
                if (!bodega.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de la bodega ya existe");
                }
            });
            existing.setNombre(request.getNombre());
        }

        if (request.getUbicacionId() != null) {
            existing.setUbicacion(findUbicacionOrThrow(request.getUbicacionId()));
        }

        existing.setUpdatedAt(java.time.LocalDateTime.now());
        return BodegaMapper.toDTO(bodegaRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Bodega existing = bodegaRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada"));
        bodegaRepository.delete(Objects.requireNonNull(existing));
    }

    @Override
    public BodegaDTO findById(Long id) {
        return bodegaRepository.findById(Objects.requireNonNull(id))
                .map(BodegaMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bodega no encontrada"));
    }

    @Override
    public List<BodegaDTO> findAll() {
        return bodegaRepository.findAll().stream()
                .map(BodegaMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Ubicacion findUbicacionOrThrow(Long ubicacionId) {
        return ubicacionRepository.findById(Objects.requireNonNull(ubicacionId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ubicacion no encontrada"));
    }

    private void validateCreateRequest(CrearBodegaDTO request) {
        if (request == null || isBlank(request.getNombre()) || request.getUbicacionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre y ubicacionId son requeridos");
        }
    }

    private void validateUpdateRequest(ActualizarBodegaDTO request) {
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
