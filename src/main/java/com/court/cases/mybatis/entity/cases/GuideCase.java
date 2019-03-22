package com.court.cases.mybatis.entity.cases;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "guide_case")
public class GuideCase {

    @Id
    private String id;
    private String title;
    private String court;
    private String updateTime;
    private String detail;

}
