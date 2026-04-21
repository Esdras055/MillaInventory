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
import com.milla.inventario.dto.stock.ActualizarStockDTO;
import com.milla.inventario.dto.stock.BodegaProductoDTO;
import com.milla.inventario.dto.stock.CrearBodegaProductoDTO;
import com.milla.inventario.service.IBodegaProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bodegas-productos")
@RequiredArgsConstructor
@Tag(name = "Bodegas Productos", description = "Gestion de stock de productos por bodega")
public class BodegaProductoController {

    private final IBodegaProductoService bodegaProductoService;

    @PostMapping
    @Operation(summary = "Crear stock", description = "Registra la cantidad inicial de un producto en una bodega.")
    public ResponseEntity<BodegaProductoDTO> create(@RequestBody CrearBodegaProductoDTO request) {
        BodegaProductoDTO created = bodegaProductoService.create(request);
        return ResponseEntity.created(URI.create("/api/bodegas-productos/" + created.getId())).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar stock", description = "Obtiene todo el stock registrado por producto y bodega.")
    public ResponseEntity<List<BodegaProductoDTO>> getAll() {
        return ResponseEntity.ok(bodegaProductoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener stock por id", description = "Consulta un registro de stock especifico.")
    public ResponseEntity<BodegaProductoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaProductoService.findById(id));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar stock por producto", description = "Obtiene el stock de un producto en todas las bodegas.")
    public ResponseEntity<List<BodegaProductoDTO>> getByProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(bodegaProductoService.findByProductoId(productoId));
    }

    @GetMapping("/bodega/{bodegaId}")
    @Operation(summary = "Listar stock por bodega", description = "Obtiene el stock de todos los productos de una bodega.")
    public ResponseEntity<List<BodegaProductoDTO>> getByBodegaId(@PathVariable Long bodegaId) {
        return ResponseEntity.ok(bodegaProductoService.findByBodegaId(bodegaId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar stock", description = "Actualiza la cantidad de un producto en una bodega.")
    public ResponseEntity<BodegaProductoDTO> update(@PathVariable Long id, @RequestBody ActualizarStockDTO request) {
        return ResponseEntity.ok(bodegaProductoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar stock", description = "Elimina un registro de stock por producto y bodega.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        bodegaProductoService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Stock eliminado correctamente"));
    }
}
