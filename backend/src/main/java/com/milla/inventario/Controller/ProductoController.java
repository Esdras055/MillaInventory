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
import com.milla.inventario.dto.producto.ActualizarProductoDTO;
import com.milla.inventario.dto.producto.CrearProductoDTO;
import com.milla.inventario.dto.producto.ProductoDTO;
import com.milla.inventario.service.IProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "CRUD de productos del inventario")
public class ProductoController {

    private final IProductoService productoService;

    @PostMapping
    @Operation(summary = "Crear producto", description = "Registra un nuevo producto en el sistema.")
    public ResponseEntity<ProductoDTO> create(@Valid @RequestBody CrearProductoDTO request) {
        ProductoDTO created = productoService.create(request);
        URI location = Objects.requireNonNull(URI.create("/api/productos/" + created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos registrados.")
    public ResponseEntity<List<ProductoDTO>> getAll() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por id", description = "Consulta un producto especifico mediante su identificador.")
    public ResponseEntity<ProductoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente.")
    public ResponseEntity<ProductoDTO> update(@PathVariable Long id, @Valid @RequestBody ActualizarProductoDTO request) {
        return ResponseEntity.ok(productoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto existente del sistema.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Producto eliminado correctamente"));
    }
}
