package com.milla.inventario.Controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milla.inventario.dto.common.ApiMessageResponse;
import com.milla.inventario.dto.rol.RolDTO;
import com.milla.inventario.dto.usuario.ActualizarUsuarioDTO;
import com.milla.inventario.dto.usuario.AsignarRolesUsuarioDTO;
import com.milla.inventario.dto.usuario.CrearUsuarioDTO;
import com.milla.inventario.dto.usuario.UsuarioDTO;
import com.milla.inventario.service.IUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "CRUD de usuarios del sistema")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Registra un nuevo usuario en el sistema.")
    public ResponseEntity<UsuarioDTO> create(@Valid @RequestBody CrearUsuarioDTO request) {
        UsuarioDTO created = usuarioService.create(request);
        URI location = Objects.requireNonNull(URI.create("/api/users/" + created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados.")
    public ResponseEntity<List<UsuarioDTO>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por id", description = "Consulta un usuario especifico mediante su identificador.")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza la informacion de un usuario existente.")
    public ResponseEntity<UsuarioDTO> update(@PathVariable Long id, @Valid @RequestBody ActualizarUsuarioDTO request) {
        return ResponseEntity.ok(usuarioService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario existente del sistema.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Usuario eliminado correctamente"));
    }

    @GetMapping("/{id}/roles")
    @Operation(summary = "Listar roles del usuario", description = "Obtiene los roles asignados a un usuario.")
    public ResponseEntity<List<RolDTO>> getRoles(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.findRolesByUserId(id));
    }

    @PutMapping("/{id}/roles")
    @Operation(summary = "Reemplazar roles del usuario", description = "Reemplaza todos los roles asignados a un usuario.")
    public ResponseEntity<List<RolDTO>> replaceRoles(
            @PathVariable Long id,
            @Valid @RequestBody AsignarRolesUsuarioDTO request) {
        return ResponseEntity.ok(usuarioService.replaceRoles(id, request));
    }

    @PostMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Agregar rol al usuario", description = "Asigna un rol especifico a un usuario.")
    public ResponseEntity<List<RolDTO>> addRole(@PathVariable Long id, @PathVariable Long roleId) {
        return ResponseEntity.ok(usuarioService.addRole(id, roleId));
    }

    @DeleteMapping("/{id}/roles/{roleId}")
    @Operation(summary = "Quitar rol del usuario", description = "Elimina un rol especifico asignado a un usuario.")
    public ResponseEntity<ApiMessageResponse> removeRole(@PathVariable Long id, @PathVariable Long roleId) {
        usuarioService.removeRole(id, roleId);
        return ResponseEntity.ok(new ApiMessageResponse("Rol removido correctamente"));
    }
}
