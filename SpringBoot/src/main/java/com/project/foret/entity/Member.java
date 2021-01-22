package com.project.foret.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String birth;
    private String deviceToken;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;

    @ManyToMany(targetEntity = Tag.class, cascade = CascadeType.ALL)
    private List<Tag> tags;
    @ManyToMany(targetEntity = Region.class, cascade = CascadeType.ALL)
    private List<Region> regions;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberPhoto> photos;

//    @ManyToMany(targetEntity = Foret.class, cascade = CascadeType.ALL)
//    private Set<Foret> forets;

    public void addTag(Tag tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
    }

    public void addRegion(Region region) {
        if (regions == null) {
            regions = new ArrayList<>();
        }
        regions.add(region);
    }

    public void addPhoto(MemberPhoto memberPhoto) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(memberPhoto);
        memberPhoto.setMember(this);
    }

}
