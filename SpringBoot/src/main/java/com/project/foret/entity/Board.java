package com.project.foret.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int type;
    private int hit;
    private String subject;
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date reg_date;
    private Date edit_date;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "foret_id")
    private Foret foret;
}
