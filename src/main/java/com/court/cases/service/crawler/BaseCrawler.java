package com.court.cases.service.crawler;

import com.court.cases.mybatis.mapper.cases.CasesMapper;
import com.court.cases.mybatis.mapper.cases.ExecuteCaseMapper;
import com.court.cases.mybatis.mapper.cases.GuideCaseMapper;
import com.court.cases.mybatis.mapper.cases.ReferenceCaseMapper;
import com.court.cases.service.ProxyService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseCrawler {

    @Autowired
    CasesMapper casesMapper;
    @Autowired
    ExecuteCaseMapper executeCaseMapper;
    @Autowired
    GuideCaseMapper guideCaseMapper;
    @Autowired
    ReferenceCaseMapper referenceCaseMapper;
    @Autowired
    ProxyService proxyService;

    public abstract void crawler();

    public String cleanHtml(String html) {
        Document doc = Jsoup.parse(html);
        doc.select("link").remove();
        doc.select("script").remove();
        doc.select("div.fd-header").remove();
        doc.select("div.fd-nav").remove();
        doc.select("div.fd-foot").remove();
        doc.select(".fd-tit-add").remove();
        doc.select("div.fd-file-tips").remove();
        doc.select("div.fd-file").remove();
        return doc.html();
    }

}
