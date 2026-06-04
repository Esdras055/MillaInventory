package com.milla.inventario.dto.usuario;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String username;
    private Long id;
    private String name;
    private List<String> roles;
}
