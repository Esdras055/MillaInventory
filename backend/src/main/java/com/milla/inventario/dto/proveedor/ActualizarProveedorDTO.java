package com.milla.inventario.dto.proveedor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActualizarProveedorDTO {
    @Pattern(regexp = ".*\\S.*", message = "Nombre no puede estar vacio")
    private String nombre;
    @Email(message = "Email debe tener un formato valido")
    @Pattern(regexp = ".*\\S.*", message = "Email no puede estar vacio")
    private String email;
    @Pattern(regexp = ".*\\S.*", message = "Telefono no puede estar vacio")
    private String telefono;
}
