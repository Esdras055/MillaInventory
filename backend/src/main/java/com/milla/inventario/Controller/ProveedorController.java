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
import com.milla.inventario.dto.proveedor.ActualizarProveedorDTO;
import com.milla.inventario.dto.proveedor.CrearProveedorDTO;
import com.milla.inventario.dto.proveedor.ProveedorDTO;
import com.milla.inventario.service.IProveedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "CRUD de proveedores para entradas de productos")
public class ProveedorController {

    private final IProveedorService proveedorService;

    @PostMapping
    @Operation(summary = "Crear proveedor", description = "Registra un nuevo proveedor en el sistema.")
    public ResponseEntity<ProveedorDTO> create(@Valid @RequestBody CrearProveedorDTO request) {
        ProveedorDTO created = proveedorService.create(request);
        URI location = Objects.requireNonNull(URI.create("/api/proveedores/" + created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar proveedores", description = "Obtiene todos los proveedores registrados.")
    public ResponseEntity<List<ProveedorDTO>> getAll() {
        return ResponseEntity.ok(proveedorService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener proveedor por id", description = "Consulta un proveedor especifico mediante su identificador.")
    public ResponseEntity<ProveedorDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(proveedorService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proveedor", description = "Actualiza los datos de un proveedor existente.")
    public ResponseEntity<ProveedorDTO> update(@PathVariable Long id, @Valid @RequestBody ActualizarProveedorDTO request) {
        return ResponseEntity.ok(proveedorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor existente del sistema.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        proveedorService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Proveedor eliminado correctamente"));
    }
}
