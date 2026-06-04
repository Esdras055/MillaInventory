package com.milla.inventario.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milla.inventario.dto.rol.RolDTO;
import com.milla.inventario.mapper.RolMapper;
import com.milla.inventario.repository.RolRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Consulta de roles del sistema")
public class RolController {

    private final RolRepository rolRepository;

    @GetMapping
    @Operation(summary = "Listar roles", description = "Obtiene todos los roles disponibles para asignacion de usuarios.")
    public ResponseEntity<List<RolDTO>> getAll() {
        return ResponseEntity.ok(
                rolRepository.findAll().stream()
                        .map(RolMapper::toDTO)
                        .toList()
        );
    }
}
