package com.court.cases.mybatis.entity.cases;

import com.court.cases.model.CourtNoticePage;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;

@Data
@Table(name = "court_notice")
@NoArgsConstructor
public class CourtNotice {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Id
    private String id;
    private String title;
    private String company;
    private String content;
    private String province;
    private String time;

    public CourtNotice(CourtNoticePage notInDbCase) {
        this.id = notInDbCase.getCBh();
        this.title = notInDbCase.getCbt();
        this.company = notInDbCase.getEsFymc();
        this.content = notInDbCase.getCnr();
        this.province = notInDbCase.getSf();
        this.time = SIMPLE_DATE_FORMAT.format(notInDbCase.getDtUpdatetime());
    }

}
