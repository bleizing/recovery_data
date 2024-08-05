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
@Table(name = "requests")
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private Users users;
    private String operations;
    private String regions;

    @Column(name = "table_target")
    private String tables;
    @Column(name = "columns_target")
    private String columns;

    @OneToMany(mappedBy = "requests", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SetValue> setValue;

    @OneToMany(mappedBy = "requests", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SetConditions> conditions;
    //add column for location_file
    @Column(name = "file_location")
    private String locationFile;
}
