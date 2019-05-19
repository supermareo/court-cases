package com.court.cases.mybatis.mapper.cases;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CasesMapper {

    List<Integer> selectAllIds();

    List<String> selectAllcBh();

    List<String> selectAllcBh2();

    List<String> selectAllNoticeIds();

}
