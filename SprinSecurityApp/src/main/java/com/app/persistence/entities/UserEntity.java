package com.app.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_No_Expired")
    private boolean accountNoExpired;

    @Column(name = "account_No_Locked")
    private boolean accountNoLocked;

    @Column(name = "credential_No_Expired")
    private boolean credentialNoExpired;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //cuando llamo un usuario me carga todos los roles que tiene, guarda un usuario en la tabla va guardar todos los roles asociados
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<RoleEntity> roles= new HashSet<>();//no permite datos repetidos

}
