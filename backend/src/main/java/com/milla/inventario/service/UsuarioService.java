package com.milla.inventario.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.usuario.ActualizarUsuarioDTO;
import com.milla.inventario.dto.usuario.CrearUsuarioDTO;
import com.milla.inventario.dto.usuario.UsuarioDTO;
import com.milla.inventario.entity.Usuario;
import com.milla.inventario.mapper.UsuarioMapper;
import com.milla.inventario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO create(CrearUsuarioDTO request) {
        usuarioRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya existe");
        });

        Usuario user = UsuarioMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Usuario saved = usuarioRepository.save(user);
        return UsuarioMapper.toDTO(saved);
    }

    @Override
    public UsuarioDTO update(Long id, ActualizarUsuarioDTO request) {
        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (request.getName() != null) existing.setName(request.getName());
        if (request.getUsername() != null && !request.getUsername().equals(existing.getUsername())) {
            usuarioRepository.findByUsername(request.getUsername()).ifPresent(u -> {
                if (!u.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya existe");
                }
            });
            existing.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            existing.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getEnabled() != null) existing.setEnabled(request.getEnabled());
        if (request.getAccountNonLocked() != null) existing.setAccountNonLocked(request.getAccountNonLocked());

        existing.setUpdatedAt(new java.util.Date());

        Usuario updated = usuarioRepository.save(existing);
        return UsuarioMapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuarioRepository.delete(existing);
    }

    @Override
    public UsuarioDTO findById(Long id) {
        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return UsuarioMapper.toDTO(existing);
    }

    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }
}
