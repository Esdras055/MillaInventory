package com.milla.inventario.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.milla.inventario.dto.rol.RolDTO;
import com.milla.inventario.dto.usuario.ActualizarUsuarioDTO;
import com.milla.inventario.dto.usuario.AsignarRolesUsuarioDTO;
import com.milla.inventario.dto.usuario.CrearUsuarioDTO;
import com.milla.inventario.dto.usuario.UsuarioDTO;
import com.milla.inventario.entity.Rol;
import com.milla.inventario.entity.Usuario;
import com.milla.inventario.entity.UsuarioRol;
import com.milla.inventario.mapper.RolMapper;
import com.milla.inventario.mapper.UsuarioMapper;
import com.milla.inventario.repository.RolRepository;
import com.milla.inventario.repository.UsuarioRolRepository;
import com.milla.inventario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService implements IUsuarioService {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO create(CrearUsuarioDTO request) {
        validateCreateRequest(request);

        usuarioRepository.findByUsername(request.getUsername()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya existe");
        });

        Usuario user = UsuarioMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Usuario saved = usuarioRepository.save(user);
        assignRoles(saved.getId(), resolveCreateRoleIds(request.getRoleIds()));
        return UsuarioMapper.toDTO(saved);
    }

    @Override
    public UsuarioDTO update(Long id, ActualizarUsuarioDTO request) {
        validateUpdateRequest(request);

        Usuario existing = usuarioRepository.findById(Objects.requireNonNull(id))
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
        Usuario existing = usuarioRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuarioRepository.delete(Objects.requireNonNull(existing));
    }

    @Override
    public UsuarioDTO findById(Long id) {
        Usuario existing = usuarioRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return UsuarioMapper.toDTO(existing);
    }

    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RolDTO> findRolesByUserId(Long userId) {
        Long id = Objects.requireNonNull(userId);
        ensureUserExists(id);
        return usuarioRolRepository.findByUserId(id).stream()
                .map(usuarioRol -> rolRepository.findById(usuarioRol.getRoleId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado")))
                .map(RolMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RolDTO> replaceRoles(Long userId, AsignarRolesUsuarioDTO request) {
        validateAssignRolesRequest(request);
        Long id = Objects.requireNonNull(userId);
        ensureUserExists(id);

        List<Long> roleIds = normalizeRoleIds(request.getRoleIds());
        findRolesOrThrow(roleIds);
        List<UsuarioRol> currentRoles = Objects.requireNonNull(usuarioRolRepository.findByUserId(id));
        usuarioRolRepository.deleteAll(currentRoles);
        assignRoles(id, roleIds);
        return findRolesByUserId(id);
    }

    @Override
    public List<RolDTO> addRole(Long userId, Long roleId) {
        Long id = Objects.requireNonNull(userId);
        Long role = Objects.requireNonNull(roleId);
        ensureUserExists(id);
        ensureRoleExists(role);

        usuarioRolRepository.findByUserIdAndRoleId(id, role)
                .orElseGet(() -> usuarioRolRepository.save(new UsuarioRol(id, role)));
        return findRolesByUserId(id);
    }

    @Override
    public void removeRole(Long userId, Long roleId) {
        Long id = Objects.requireNonNull(userId);
        Long role = Objects.requireNonNull(roleId);
        ensureUserExists(id);

        UsuarioRol usuarioRol = usuarioRolRepository.findByUserIdAndRoleId(id, role)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no tiene asignado ese rol"));
        usuarioRolRepository.delete(Objects.requireNonNull(usuarioRol));
    }

    private void validateCreateRequest(CrearUsuarioDTO request) {
        if (request == null
                || isBlank(request.getName())
                || isBlank(request.getUsername())
                || isBlank(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name, username y password son requeridos");
        }
    }

    private void validateUpdateRequest(ActualizarUsuarioDTO request) {
        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud es requerido");
        }
        if (request.getName() != null && request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name no puede estar vacio");
        }
        if (request.getUsername() != null && request.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username no puede estar vacio");
        }
        if (request.getPassword() != null && request.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password no puede estar vacio");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private List<Long> resolveCreateRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            Rol defaultRole = rolRepository.findByName(DEFAULT_ROLE)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol por defecto no encontrado"));
            return List.of(defaultRole.getId());
        }
        List<Long> normalizedRoleIds = normalizeRoleIds(roleIds);
        findRolesOrThrow(normalizedRoleIds);
        return normalizedRoleIds;
    }

    private void assignRoles(Long userId, List<Long> roleIds) {
        Long id = Objects.requireNonNull(userId);
        roleIds.forEach(roleId -> usuarioRolRepository.save(new UsuarioRol(id, Objects.requireNonNull(roleId))));
    }

    private List<Long> normalizeRoleIds(List<Long> roleIds) {
        return roleIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private List<Rol> findRolesOrThrow(List<Long> roleIds) {
        if (roleIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe asignar al menos un rol");
        }
        List<Rol> roles = rolRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Uno o mas roles no fueron encontrados");
        }
        return roles;
    }

    private void ensureUserExists(Long userId) {
        if (!usuarioRepository.existsById(Objects.requireNonNull(userId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    private void ensureRoleExists(Long roleId) {
        if (!rolRepository.existsById(Objects.requireNonNull(roleId))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado");
        }
    }

    private void validateAssignRolesRequest(AsignarRolesUsuarioDTO request) {
        if (request == null || request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe asignar al menos un rol");
        }
    }
}
