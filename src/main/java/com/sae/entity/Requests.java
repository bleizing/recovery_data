package com.sae.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request")
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users users;
    private String name;
    private String operations;
    private String regions;

    @Column(name = "table_target")
    private String tables;
    @Column(name = "columns_target")
    private String columns;

    @OneToMany(mappedBy = "requests", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SettersVal> params;

    @OneToMany(mappedBy = "requests", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conditions> conditions;
}
