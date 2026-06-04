package com.milla.inventario.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActualizarUsuarioDTO {
    @Pattern(regexp = ".*\\S.*", message = "Name no puede estar vacio")
    private String name;
    @Pattern(regexp = ".*\\S.*", message = "Username no puede estar vacio")
    @Email(message = "Username debe ser un correo valido")
    private String username;
    @Pattern(regexp = ".*\\S.*", message = "Password no puede estar vacio")
    private String password;
    private Boolean enabled;
    private Boolean accountNonLocked;
}
