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
import com.milla.inventario.dto.entrada.ActualizarEntradaDTO;
import com.milla.inventario.dto.entrada.CrearEntradaDTO;
import com.milla.inventario.dto.entrada.EntradaDTO;
import com.milla.inventario.service.IEntradaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/entradas")
@RequiredArgsConstructor
@Tag(name = "Entradas", description = "Registro de entradas de productos al inventario")
public class EntradaController {

    private final IEntradaService entradaService;

    @PostMapping
    @Operation(summary = "Crear entrada", description = "Registra una entrada de producto y aumenta el stock de la bodega.")
    public ResponseEntity<EntradaDTO> create(@Valid @RequestBody CrearEntradaDTO request) {
        EntradaDTO created = entradaService.create(request);
        return ResponseEntity.created(URI.create("/api/entradas/" + created.getId())).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar entradas", description = "Obtiene todas las entradas registradas.")
    public ResponseEntity<List<EntradaDTO>> getAll() {
        return ResponseEntity.ok(entradaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener entrada por id", description = "Consulta una entrada especifica mediante su identificador.")
    public ResponseEntity<EntradaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(entradaService.findById(id));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar entradas por producto", description = "Obtiene las entradas registradas para un producto.")
    public ResponseEntity<List<EntradaDTO>> getByProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(entradaService.findByProductoId(productoId));
    }

    @GetMapping("/bodega/{bodegaId}")
    @Operation(summary = "Listar entradas por bodega", description = "Obtiene las entradas registradas en una bodega.")
    public ResponseEntity<List<EntradaDTO>> getByBodegaId(@PathVariable Long bodegaId) {
        return ResponseEntity.ok(entradaService.findByBodegaId(bodegaId));
    }

    @GetMapping("/proveedor/{proveedorId}")
    @Operation(summary = "Listar entradas por proveedor", description = "Obtiene las entradas registradas para un proveedor.")
    public ResponseEntity<List<EntradaDTO>> getByProveedorId(@PathVariable Long proveedorId) {
        return ResponseEntity.ok(entradaService.findByProveedorId(proveedorId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar entrada", description = "Actualiza una entrada y reajusta el stock correspondiente.")
    public ResponseEntity<EntradaDTO> update(@PathVariable Long id, @Valid @RequestBody ActualizarEntradaDTO request) {
        return ResponseEntity.ok(entradaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar entrada", description = "Elimina una entrada y revierte el stock correspondiente.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        entradaService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Entrada eliminada correctamente"));
    }
}
