package com.project.foret.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ForetModel {
    private String name;
    private String introduce;
    private int max_member;
    private Date reg_date;
    private List<TagModel> tags;
    private List<RegionModel> regions;
    private List<PhotoModel> photos;
    private List<MemberModel> members;
}
