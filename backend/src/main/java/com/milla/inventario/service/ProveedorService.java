package com.milla.inventario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.proveedor.ActualizarProveedorDTO;
import com.milla.inventario.dto.proveedor.CrearProveedorDTO;
import com.milla.inventario.dto.proveedor.ProveedorDTO;
import com.milla.inventario.entity.Proveedor;
import com.milla.inventario.mapper.ProveedorMapper;
import com.milla.inventario.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProveedorService implements IProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Override
    public ProveedorDTO create(CrearProveedorDTO request) {
        validateCreateRequest(request);
        validateUniqueCreate(request);

        return ProveedorMapper.toDTO(proveedorRepository.save(ProveedorMapper.toEntity(request)));
    }

    @Override
    public ProveedorDTO update(Long id, ActualizarProveedorDTO request) {
        validateUpdateRequest(request);

        Proveedor existing = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));

        if (request.getNombre() != null && !request.getNombre().equals(existing.getNombre())) {
            proveedorRepository.findByNombre(request.getNombre()).ifPresent(proveedor -> {
                if (!proveedor.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre del proveedor ya existe");
                }
            });
            existing.setNombre(request.getNombre());
        }

        if (request.getEmail() != null && !request.getEmail().equals(existing.getEmail())) {
            proveedorRepository.findByEmail(request.getEmail()).ifPresent(proveedor -> {
                if (!proveedor.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El email del proveedor ya existe");
                }
            });
            existing.setEmail(request.getEmail());
        }

        if (request.getTelefono() != null && !request.getTelefono().equals(existing.getTelefono())) {
            proveedorRepository.findByTelefono(request.getTelefono()).ifPresent(proveedor -> {
                if (!proveedor.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El telefono del proveedor ya existe");
                }
            });
            existing.setTelefono(request.getTelefono());
        }

        existing.setUpdatedAt(new java.util.Date());
        return ProveedorMapper.toDTO(proveedorRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        Proveedor existing = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
        proveedorRepository.delete(existing);
    }

    @Override
    public ProveedorDTO findById(Long id) {
        return proveedorRepository.findById(id)
                .map(ProveedorMapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado"));
    }

    @Override
    public List<ProveedorDTO> findAll() {
        return proveedorRepository.findAll().stream()
                .map(ProveedorMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validateCreateRequest(CrearProveedorDTO request) {
        if (request == null
                || isBlank(request.getNombre())
                || isBlank(request.getEmail())
                || isBlank(request.getTelefono())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre, email y telefono son requeridos");
        }
    }

    private void validateUpdateRequest(ActualizarProveedorDTO request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud es requerido");
        }
        if (request.getNombre() != null && request.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre no puede estar vacio");
        }
        if (request.getEmail() != null && request.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email no puede estar vacio");
        }
        if (request.getTelefono() != null && request.getTelefono().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telefono no puede estar vacio");
        }
    }

    private void validateUniqueCreate(CrearProveedorDTO request) {
        proveedorRepository.findByNombre(request.getNombre()).ifPresent(proveedor -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre del proveedor ya existe");
        });
        proveedorRepository.findByEmail(request.getEmail()).ifPresent(proveedor -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El email del proveedor ya existe");
        });
        proveedorRepository.findByTelefono(request.getTelefono()).ifPresent(proveedor -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El telefono del proveedor ya existe");
        });
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
