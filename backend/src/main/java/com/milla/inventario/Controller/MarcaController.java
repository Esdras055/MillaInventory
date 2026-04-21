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
import com.milla.inventario.dto.marca.ActualizarMarcaDTO;
import com.milla.inventario.dto.marca.CrearMarcaDTO;
import com.milla.inventario.dto.marca.MarcaDTO;
import com.milla.inventario.service.IMarcaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/marcas")
@RequiredArgsConstructor
@Tag(name = "Marcas", description = "CRUD de marcas para los productos")
public class MarcaController {

    private final IMarcaService marcaService;

    @PostMapping
    @Operation(summary = "Crear marca", description = "Registra una nueva marca en el sistema.")
    public ResponseEntity<MarcaDTO> create(@RequestBody CrearMarcaDTO request) {
        MarcaDTO created = marcaService.create(request);
        return ResponseEntity.created(URI.create("/api/marcas/" + created.getId())).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar marcas", description = "Obtiene todas las marcas registradas.")
    public ResponseEntity<List<MarcaDTO>> getAll() {
        return ResponseEntity.ok(marcaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener marca por id", description = "Consulta una marca especifica mediante su identificador.")
    public ResponseEntity<MarcaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(marcaService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar marca", description = "Actualiza los datos de una marca existente.")
    public ResponseEntity<MarcaDTO> update(@PathVariable Long id, @RequestBody ActualizarMarcaDTO request) {
        return ResponseEntity.ok(marcaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar marca", description = "Elimina una marca existente del sistema.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        marcaService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Marca eliminada correctamente"));
    }
}
