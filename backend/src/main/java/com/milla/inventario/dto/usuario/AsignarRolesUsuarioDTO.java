package com.milla.inventario.dto.usuario;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AsignarRolesUsuarioDTO {
    @NotNull(message = "RoleIds es requerido")
    @Size(min = 1, message = "Debe asignar al menos un rol")
    private List<@Positive(message = "RoleId debe ser mayor que cero") Long> roleIds;
}
