package com.court.cases.mybatis.entity.cases;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "reference_case")
public class ReferenceCase {

    @Id
    private String id;
    private String title;
    private String court;
    private String updateTime;
    private String detail;

}
