package com.milla.inventario.dto.proveedor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CrearProveedorDTO {
    @NotBlank(message = "Nombre es requerido")
    private String nombre;
    @NotBlank(message = "Email es requerido")
    @Email(message = "Email debe tener un formato valido")
    private String email;
    @NotBlank(message = "Telefono es requerido")
    private String telefono;
}
