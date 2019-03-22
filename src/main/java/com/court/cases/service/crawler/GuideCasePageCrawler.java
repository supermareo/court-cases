package com.court.cases.service.crawler;

import com.alibaba.fastjson.JSONObject;
import com.court.cases.Constant;
import com.court.cases.model.GuideCasePage;
import com.court.cases.mybatis.entity.cases.GuideCase;
import com.court.cases.mybatis.mapper.cases.CasesMapper;
import com.court.cases.mybatis.mapper.cases.GuideCaseMapper;
import com.court.cases.utils.JsonUtil;
import com.court.cases.utils.OkHttpUtil;
import com.court.cases.utils.StringUtil;
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
public class GuideCasePageCrawler extends BaseCrawler {

    /**
     * 爬取并入库
     */
    @Override
    public void crawler() {
        int totalPages = 10;
        int curPage = 0;
        log.info("crawlerGuideCases start");
        while (true) {
            try {
                if (curPage >= totalPages) {
                    break;
                }
                curPage++;
                Map<String, Object> formData = new HashMap<>();
                formData.put("fbdw", "最高人民法院");
                formData.put("bt", "");
                formData.put("lx", "lzdx");
                formData.put("pageNum", "" + curPage);
                String response = OkHttpUtil.postForm(Constant.URL_ZDXAL_LIST, formData,3);
                JSONObject jo = JSONObject.parseObject(response);
                totalPages = jo.getInteger("pages");
                String dataJson = jo.getJSONArray("list").toString();
                List<GuideCasePage> data = JsonUtil.fromJson(dataJson, new TypeToken<List<GuideCasePage>>() {
                }.getType());
                if (CollectionUtils.isEmpty(data)) {
                    log.info("crawlerGuideCases complete empty data");
                    break;
                }

                //查找所有存在数据库中的案例的id
                List<String> ids = casesMapper.selectAllcBh();
                //过滤出所有不在数据库中的案例的id
                List<GuideCasePage> notInDbCases = data.stream().filter(d -> !ids.contains(d.getCBh())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(notInDbCases)) {
                    log.info("crawlerGuideCases complete all cases already in db");
                    return;
                }
                for (GuideCasePage notInDbCase : notInDbCases) {
                    GuideCase guideCase = crawlerDetail(notInDbCase);
                    //入库
                    guideCaseMapper.insert(guideCase);
                }
            } catch (Exception e) {
                log.error("crawlerGuideCases error, e=", e);
            }
        }
        log.info("crawlerGuideCases complete success");
    }


    /**
     * 爬取详情
     */
    private GuideCase crawlerDetail(GuideCasePage casePage) {
        GuideCase guideCase = new GuideCase();
        guideCase.setId(casePage.getCBh());
        guideCase.setTitle(StringUtil.ncrToCn(casePage.getCBt()));
        guideCase.setCourt(StringUtil.ncrToCn(casePage.getCFymc()));
        guideCase.setUpdateTime(casePage.getDtUpdatetime());
        String url = Constant.URL_ZDXAL_DETAIL.replace("#ID", casePage.getCBh());
        log.info("crawlerDetail start {}", url);
        String html = OkHttpUtil.get(url);
        html = cleanHtml(html);
        guideCase.setDetail(html);
        log.info("crawlerDetail complete {}, data={}", url, casePage);
        return guideCase;
    }

}
