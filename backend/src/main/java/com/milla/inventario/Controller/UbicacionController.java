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

import com.milla.inventario.dto.common.ApiMessageResponse;
import com.milla.inventario.dto.ubicacion.ActualizarUbicacionDTO;
import com.milla.inventario.dto.ubicacion.CrearUbicacionDTO;
import com.milla.inventario.dto.ubicacion.UbicacionDTO;
import com.milla.inventario.service.IUbicacionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
@Tag(name = "Ubicaciones", description = "CRUD de ubicaciones para las bodegas")
public class UbicacionController {

    private final IUbicacionService ubicacionService;

    @PostMapping
    @Operation(summary = "Crear ubicacion", description = "Registra una nueva ubicacion en el sistema.")
    public ResponseEntity<UbicacionDTO> create(@RequestBody CrearUbicacionDTO request) {
        UbicacionDTO created = ubicacionService.create(request);
        return ResponseEntity.created(URI.create("/api/ubicaciones/" + created.getId())).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar ubicaciones", description = "Obtiene todas las ubicaciones registradas.")
    public ResponseEntity<List<UbicacionDTO>> getAll() {
        return ResponseEntity.ok(ubicacionService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ubicacion por id", description = "Consulta una ubicacion especifica mediante su identificador.")
    public ResponseEntity<UbicacionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ubicacionService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar ubicacion", description = "Actualiza los datos de una ubicacion existente.")
    public ResponseEntity<UbicacionDTO> update(@PathVariable Long id, @RequestBody ActualizarUbicacionDTO request) {
        return ResponseEntity.ok(ubicacionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar ubicacion", description = "Elimina una ubicacion existente del sistema.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        ubicacionService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Ubicacion eliminada correctamente"));
    }
}
