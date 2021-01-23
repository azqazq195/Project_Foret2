package com.project.foret.model;

import lombok.Data;

import java.util.Date;

@Data
public class BoardModel {
    private int type;
    private int hit;
    private String subject;
    private String content;
    private Date reg_date;
    private Date edit_date;
}
