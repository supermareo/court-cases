package com.court.cases.service.crawler;

import com.alibaba.fastjson.JSONObject;
import com.court.cases.Constant;
import com.court.cases.model.CourtNoticePage;
import com.court.cases.mybatis.entity.cases.CourtNotice;
import com.court.cases.mybatis.mapper.cases.CourtNoticeMapper;
import com.court.cases.utils.JsonUtil;
import com.court.cases.utils.OkHttpUtil;
import com.google.gson.reflect.TypeToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CourtNoticeCrawler extends BaseCrawler {

    @Autowired
    private CourtNoticeMapper courtNoticeMapper;

    @Override
    public void crawler() {
        int curPage = 400;
        int totalPages = 400;
        int duplicateCount = 0;
        log.info("crawlerCourtNoticeCrawler start");
        while (true) {
            try {
                if (curPage > totalPages) {
                    break;
                }
                curPage++;
                Map<String, Object> formData = new HashMap<>();
                formData.put("bt", "");
                formData.put("'fydw'", "");
                formData.put("pageNum", "" + curPage);
                String url = Constant.URL_KTGG_LIST.replace("#PAGE", curPage + "");
                log.info("crawlerCourtNoticeCrawler {}", url);
                String response = OkHttpUtil.postForm(url, formData, proxyService);
                response = check(response, url, formData);
                //访问频次达到限制
                JSONObject jo = JSONObject.parseObject(response);
//                if (totalPages == 0) {
                totalPages = jo.getJSONObject("page").getInteger("pages");
//                }
                String dataList = jo.getJSONArray("data").toString();
                List<CourtNoticePage> data = JsonUtil.fromJson(dataList, new TypeToken<List<CourtNoticePage>>() {
                }.getType());
                if (CollectionUtils.isEmpty(data)) {
                    log.info("crawlerCourtNoticeCrawler complete empty data");
                    return;
                }
                //查找所有存在数据库中的案例的id
                List<String> ids = casesMapper.selectAllNoticeIds();
                //过滤出所有不在数据库中的案例的id
                List<CourtNoticePage> notInDbCases = data.stream().filter(d -> !ids.contains(d.getCBh())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(notInDbCases)) {
                    if (duplicateCount > 3) {
                        log.info("crawlerCourtNoticeCrawler complete all cases already in db");
                        return;
                    } else {
                        duplicateCount++;
                        log.info("crawlerCourtNoticeCrawler whole page duplicate, count={}, url={}", duplicateCount, url);
                        continue;
                    }
                }
                for (CourtNoticePage notInDbCase : notInDbCases) {
                    //入库
                    CourtNotice courtNotice = new CourtNotice(notInDbCase);
                    courtNoticeMapper.insert(courtNotice);
                }
                //限制爬虫频率
//                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("crawlerCourtNoticeCrawler error, e=", e);
            }
        }
        log.info("crawlerCourtNoticeCrawler complete success");
    }

    private String check(String response, String url, Map<String, Object> formData) {
        try {
            JSONObject jo = JSONObject.parseObject(response);
            boolean check = jo.containsKey("pageCheck") && jo.getBoolean("pageCheck");
            if (!check) {
                return response;
            } else {
                log.info("response={}", response);
                proxyService.refresh();
                return check(OkHttpUtil.postForm(url, formData, proxyService), url, formData);
            }
        } catch (Exception e) {
            log.info("response={}", response);
            proxyService.refresh();
            return check(OkHttpUtil.postForm(url, formData, proxyService), url, formData);
        }
    }

}
