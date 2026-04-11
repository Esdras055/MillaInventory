package com.milla.inventario.Controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milla.inventario.dto.bodega.ActualizarBodegaDTO;
import com.milla.inventario.dto.bodega.BodegaDTO;
import com.milla.inventario.dto.bodega.CrearBodegaDTO;
import com.milla.inventario.dto.common.ApiMessageResponse;
import com.milla.inventario.service.IBodegaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
@Tag(name = "Bodegas", description = "CRUD de bodegas del inventario")
public class BodegaController {

    private final IBodegaService bodegaService;

    @PostMapping
    @Operation(summary = "Crear bodega", description = "Registra una nueva bodega asociada a una ubicacion.")
    public ResponseEntity<BodegaDTO> create(@RequestBody CrearBodegaDTO request) {
        BodegaDTO created = bodegaService.create(request);
        return ResponseEntity.created(URI.create("/api/bodegas/" + created.getId())).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar bodegas", description = "Obtiene todas las bodegas registradas.")
    public ResponseEntity<List<BodegaDTO>> getAll() {
        return ResponseEntity.ok(bodegaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener bodega por id", description = "Consulta una bodega especifica mediante su identificador.")
    public ResponseEntity<BodegaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar bodega", description = "Actualiza los datos de una bodega existente.")
    public ResponseEntity<BodegaDTO> update(@PathVariable Long id, @RequestBody ActualizarBodegaDTO request) {
        return ResponseEntity.ok(bodegaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar bodega", description = "Elimina una bodega del sistema.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        bodegaService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Bodega eliminada correctamente"));
    }
}
