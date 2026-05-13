package com.milla.inventario.service;

import com.milla.inventario.dto.usuario.AuthResponseDTO;
import com.milla.inventario.dto.usuario.LoginDTO;

public interface IAuthService {
    AuthResponseDTO authenticate(LoginDTO request);

    void logout(String authorizationHeader);
}
