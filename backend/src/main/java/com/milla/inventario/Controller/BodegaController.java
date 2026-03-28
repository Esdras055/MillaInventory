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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final IBodegaService bodegaService;

    @PostMapping
    public ResponseEntity<BodegaDTO> create(@RequestBody CrearBodegaDTO request) {
        BodegaDTO created = bodegaService.create(request);
        return ResponseEntity.created(URI.create("/api/bodegas/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<List<BodegaDTO>> getAll() {
        return ResponseEntity.ok(bodegaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BodegaDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BodegaDTO> update(@PathVariable Long id, @RequestBody ActualizarBodegaDTO request) {
        return ResponseEntity.ok(bodegaService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiMessageResponse> delete(@PathVariable Long id) {
        bodegaService.delete(id);
        return ResponseEntity.ok(new ApiMessageResponse("Bodega eliminada correctamente"));
    }
}
