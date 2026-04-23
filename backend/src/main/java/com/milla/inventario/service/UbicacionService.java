package com.milla.inventario.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.ubicacion.ActualizarUbicacionDTO;
import com.milla.inventario.dto.ubicacion.CrearUbicacionDTO;
import com.milla.inventario.dto.ubicacion.UbicacionDTO;
import com.milla.inventario.entity.Ubicacion;
import com.milla.inventario.mapper.UbicacionMapper;
import com.milla.inventario.repository.UbicacionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UbicacionService implements IUbicacionService {

    private final UbicacionRepository ubicacionRepository;

    @Override
    public UbicacionDTO create(CrearUbicacionDTO request) {
        validateCreateRequest(request);

        ubicacionRepository.findByMunicipio(request.getMunicipio()).ifPresent(ubicacion -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El municipio ya existe");
        });

        Ubicacion ubicacion = Objects.requireNonNull(UbicacionMapper.toEntity(request));
        return UbicacionMapper.toDTO(ubicacionRepository.save(ubicacion));
    }

    @Override
    public UbicacionDTO update(Long id, ActualizarUbicacionDTO request) {
        validateUpdateRequest(request);

        Ubicacion existing = ubicacionRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ubicacion no encontrada"));

        if (request.getMunicipio() != null && !request.getMunicipio().equals(existing.getMunicipio())) {
            ubicacionRepository.findByMunicipio(request.getMunicipio()).ifPresent(ubicacion -> {
                if (!ubicacion.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El municipio ya existe");
                }
            });
            existing.setMunicipio(request.getMunicipio());
        }

        existing.setUpdatedAt(java.time.LocalDateTime.now());
        return UbicacionMapper.toDTO(ubicacionRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Ubicacion existing = ubicacionRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ubicacion no encontrada"));
        ubicacionRepository.delete(Objects.requireNonNull(existing));
    }

    @Override
    public UbicacionDTO findById(Long id) {
        return ubicacionRepository.findById(Objects.requireNonNull(id))
                .map(UbicacionMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ubicacion no encontrada"));
    }

    @Override
    public List<UbicacionDTO> findAll() {
        return ubicacionRepository.findAll().stream()
                .map(UbicacionMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validateCreateRequest(CrearUbicacionDTO request) {
        if (request == null || request.getMunicipio() == null || request.getMunicipio().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Municipio es requerido");
        }
    }

    private void validateUpdateRequest(ActualizarUbicacionDTO request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud es requerido");
        }
        if (request.getMunicipio() != null && request.getMunicipio().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Municipio no puede estar vacio");
        }
    }
}
