package com.milla.inventario.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class UsuarioRolId implements Serializable {
    private Long userId;
    private Long roleId;

}
