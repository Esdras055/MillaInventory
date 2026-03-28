package com.milla.inventario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@IdClass(UsuarioRolId.class)
public class UsuarioRol {
    @Id
    @Column(name = "userId")
    private Long userId;
    @Id
    @Column(name = "roleId")
    private long roleId;

}
