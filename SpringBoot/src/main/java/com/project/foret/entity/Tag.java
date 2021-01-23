package com.project.foret.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;
    @ManyToMany(targetEntity = Member.class, mappedBy = "tags", cascade = CascadeType.ALL)
    private List<Member> members;
    // private Set<Foret> forets;
}
