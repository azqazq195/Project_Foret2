package com.project.foret.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String regionSi;
    private String regionGu;

    private Set<Member> members;
    // private Set<Foret> forets;
}
