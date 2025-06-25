package com.agrocredito.domain.model.entity.user;

import com.agrocredito.domain.model.entity.auth.TokenEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public final class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(length = 20)
    private String telefono;

    @Column(length = 30)
    private String rol;

    @Column(length = 1000)
    private String ciudad;

    @Column(length = 100)
    private String direccion;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenEntity> tokenEntities;
}
