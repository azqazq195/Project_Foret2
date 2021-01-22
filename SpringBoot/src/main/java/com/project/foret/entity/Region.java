package com.project.foret.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "region")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String regionSi;
    private String regionGu;

    @ManyToMany(targetEntity = Member.class, mappedBy = "tags", cascade = CascadeType.ALL)
    private Set<Member> members;
    // private Set<Foret> forets;

    public void addMember(Member member) {
        if (members == null) {
            members = new HashSet<>();
        }
        members.add(member);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }
}
