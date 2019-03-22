package com.court.cases.service.crawler;

import com.alibaba.fastjson.JSONObject;
import com.court.cases.Constant;
import com.court.cases.model.ReferenceCasePage;
import com.court.cases.mybatis.entity.cases.ReferenceCase;
import com.court.cases.utils.JsonUtil;
import com.court.cases.utils.OkHttpUtil;
import com.court.cases.utils.StringUtil;
import com.google.gson.reflect.TypeToken;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReferenceCasePageCrawler extends BaseCrawler {

    /**
     * 爬取并入库
     */
    @Override
    public void crawler() {
        int totalPages = 10;
        int curPage = 0;
        while (true) {
            try {
                if (curPage >= totalPages) {
                    break;
                }
                curPage++;
                Map<String, Object> formData = new HashMap<>();
                formData.put("fbdw", "");
                formData.put("bt", "");
                formData.put("lx", "lckx");
                formData.put("pageNum", "" + curPage);
                String response = OkHttpUtil.postForm(Constant.URL_CKXAL_LIST, formData);
                JSONObject jo = JSONObject.parseObject(response);
                totalPages = jo.getInteger("pages");
                String dataJson = jo.getJSONArray("list").toString();
                List<ReferenceCasePage> data = JsonUtil.fromJson(dataJson, new TypeToken<List<ReferenceCasePage>>() {
                }.getType());
                if (CollectionUtils.isEmpty(data)) {
                    log.info("crawlerReferenceCases complete empty data");
                    break;
                }

                //查找所有存在数据库中的案例的id
                List<String> ids = casesMapper.selectAllcBh2();
                //过滤出所有不在数据库中的案例的id
                List<ReferenceCasePage> notInDbCases = data.stream().filter(d -> !ids.contains(d.getCBh())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(notInDbCases)) {
                    log.info("crawlerReferenceCases complete all cases already in db");
                    return;
                }
                for (ReferenceCasePage notInDbCase : notInDbCases) {
                    ReferenceCase referenceCase = crawlerDetail(notInDbCase);
                    //入库
                    referenceCaseMapper.insert(referenceCase);
                }
                log.info("crawlerReferenceCases complete success");
            } catch (Exception e) {
                log.error("crawlerReferenceCases error, e=", e);
            }
        }
    }


    /**
     * 爬取详情
     */
    private ReferenceCase crawlerDetail(ReferenceCasePage casePage) {
        ReferenceCase referenceCase = new ReferenceCase();
        referenceCase.setId(casePage.getCBh());
        referenceCase.setTitle(StringUtil.ncrToCn(casePage.getCBt()));
        referenceCase.setCourt(StringUtil.ncrToCn(casePage.getCFymc()));
        referenceCase.setUpdateTime(casePage.getDtUpdatetime());
        String url = Constant.URL_CKXAL_DETAIL.replace("#ID", casePage.getCBh());
        log.info("crawlerDetail start {}", url);
        String html = OkHttpUtil.get(url);
        html = cleanHtml(html);
        referenceCase.setDetail(html);
        log.info("crawlerDetail complete {}, data={}", url, casePage);
        return referenceCase;
    }

}
