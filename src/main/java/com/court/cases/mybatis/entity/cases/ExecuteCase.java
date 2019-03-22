package com.court.cases.mybatis.entity.cases;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "execute_case")
public class ExecuteCase {

    @Id
    private Integer id;
    private String brief;
    private String fileName;
    private String filePath;
    private String title;
    private String detail;

}
