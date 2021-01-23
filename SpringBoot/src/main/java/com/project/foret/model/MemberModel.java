package com.project.foret.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MemberModel {
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String birth;
    private String deviceToken;
    private Date reg_date;
    private List<TagModel> tags;
    private List<RegionModel> regions;
    private List<PhotoModel> photos;
}
