package com.project.foret.model;

import com.project.foret.entity.Member;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
public class MemberPhotoModel {
    private String dir;
    private String filename;
    private String originname;
    private int filesize;
    private String filetype;
    private Date reg_date;
}
