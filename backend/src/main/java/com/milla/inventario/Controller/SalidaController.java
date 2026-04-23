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
import com.milla.inventario.dto.salida.ActualizarSalidaDTO;
import com.milla.inventario.dto.salida.CrearSalidaDTO;
import com.milla.inventario.dto.salida.SalidaDTO;
import com.milla.inventario.service.ISalidaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/salidas")
@RequiredArgsConstructor
@Tag(name = "Salidas", description = "Registro de salidas de productos del inventario")
public class SalidaController {

    private final ISalidaService salidaService;

    @PostMapping
    @Operation(summary = "Crear salida", description = "Registra una salida de producto y disminuye el stock de la bodega.")
    public ResponseEntity<SalidaDTO> create(@Valid @RequestBody CrearSalidaDTO request) {
        SalidaDTO created = salidaService.crear(request);
        return ResponseEntity.created(URI.create("/api/salidas/" + created.getId())).body(created);
    }

    @GetMapping
    @Operation(summary = "Listar salidas", description = "Obtiene todas las salidas registradas.")
    public ResponseEntity<List<SalidaDTO>> getAll() {
        return ResponseEntity.ok(salidaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener salida por id", description = "Consulta una salida especifica mediante su identificador.")
    public ResponseEntity<SalidaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(salidaService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    @Operation(summary = "Listar salidas por producto", description = "Obtiene las salidas registradas para un producto.")
    public ResponseEntity<List<SalidaDTO>> getByProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(salidaService.listarPorProducto(productoId));
    }

    @GetMapping("/bodega/{bodegaId}")
    @Operation(summary = "Listar salidas por bodega", description = "Obtiene las salidas registradas en una bodega.")
    public ResponseEntity<List<SalidaDTO>> getByBodegaId(@PathVariable Long bodegaId) {
        return ResponseEntity.ok(salidaService.listarPorBodega(bodegaId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar salida", description = "Actualiza una salida y reajusta el stock correspondiente.")
    public ResponseEntity<SalidaDTO> update(@PathVariable Long id, @Valid @RequestBody ActualizarSalidaDTO request) {
        return ResponseEntity.ok(salidaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar salida", description = "Elimina una salida y devuelve el stock correspondiente.")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        salidaService.eliminar(id);
        return ResponseEntity.ok(new ApiMessageResponse("Salida eliminada correctamente"));
    }
}
