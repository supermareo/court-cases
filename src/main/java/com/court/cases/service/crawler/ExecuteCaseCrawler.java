package com.court.cases.service.crawler;

import com.alibaba.fastjson.JSONArray;
import com.court.cases.Constant;
import com.court.cases.mybatis.entity.cases.ExecuteCase;
import com.court.cases.utils.JsonUtil;
import com.court.cases.utils.OkHttpUtil;
import com.google.gson.reflect.TypeToken;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

/**
 * 最高法院执行案例爬虫
 */
@Slf4j
@Service
public class ExecuteCaseCrawler extends BaseCrawler {


    /**
     * 爬取并入库
     */
    @Override
    public void crawler() {
        try {
            log.info("crawlerExecuteCases start");
            String response = OkHttpUtil.get(Constant.URL_ZXGK, proxyService);
            JSONArray ja = JSONArray.parseArray(response);
            response = ja.getJSONObject(0).getJSONArray("list").toString();
            List<ExecuteCase> data = JsonUtil.fromJson(response, new TypeToken<List<ExecuteCase>>() {
            }.getType());
            if (CollectionUtils.isEmpty(data)) {
                log.info("crawlerExecuteCases complete empty data");
                return;
            }
            //查找所有存在数据库中的案例的id
            List<Integer> ids = casesMapper.selectAllIds();
            //过滤出所有不在数据库中的案例的id
            List<ExecuteCase> notInDbCases = data.stream().filter(d -> !ids.contains(d.getId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(notInDbCases)) {
                log.info("crawlerExecuteCases complete all cases already in db");
                return;
            }
            for (ExecuteCase notInDbCase : notInDbCases) {
                crawlerDetail(notInDbCase);
                //入库
                executeCaseMapper.insert(notInDbCase);
                //限制爬虫频率
                Thread.sleep(500);
            }
            log.info("crawlerExecuteCases complete success");
        } catch (Exception e) {
            log.error("crawlerExecuteCases error, e=", e);
        }
    }

    /**
     * 爬取详情
     */
    private void crawlerDetail(ExecuteCase casePage) {
        String url = Constant.URL_ZXGK_DETAIL.replace("#PATH", casePage.getFilePath()).replace("#FILENAME", casePage.getFileName());
        log.info("crawlerDetail start {}", url);
        String html = OkHttpUtil.get(url, proxyService);
        casePage.setDetail(html);
        log.info("crawlerDetail complete {}, data={}", url, casePage);
    }

}
