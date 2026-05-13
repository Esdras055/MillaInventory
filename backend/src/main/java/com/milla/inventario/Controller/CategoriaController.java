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

import com.milla.inventario.dto.categoria.ActualizarCategoriaDTO;
import com.milla.inventario.dto.categoria.CategoriaDTO;
import com.milla.inventario.dto.categoria.CrearCategoriaDTO;
import com.milla.inventario.dto.common.ApiMessageResponse;
import com.milla.inventario.service.ICategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "CRUD de categorias para los productos")
public class CategoriaController {

    private final ICategoriaService categoriaService;

    @PostMapping
    @Operation(summary = "Crear categoria", description = "Registra una nueva categoria en el sistema.")
    public ResponseEntity<CategoriaDTO> create(@Valid @RequestBody CrearCategoriaDTO request) {
        CategoriaDTO created = categoriaService.create(request);
        URI location = Objects.requireNonNull(URI.create("/api/categorias/" + created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Obtiene todas las categorias registradas.")
    public ResponseEntity<List<CategoriaDTO>> getAll() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoria por id", description = "Consulta una categoria especifica mediante su identificador.")
    public ResponseEntity<CategoriaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoria", description = "Actualiza los datos de una categoria existente.")
    public ResponseEntity<CategoriaDTO> update(@PathVariable Long id, @Valid @RequestBody ActualizarCategoriaDTO request) {
        return ResponseEntity.ok(categoriaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoria", description = "Elimina una categoria existente del sistema.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Categoría eliminada correctamente"));
    }
}
