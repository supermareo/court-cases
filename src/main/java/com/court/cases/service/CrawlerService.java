package com.court.cases.service;

import com.court.cases.service.crawler.CourtNoticeCrawler;
import com.court.cases.service.crawler.ExecuteCaseCrawler;
import com.court.cases.service.crawler.GuideCasePageCrawler;
import com.court.cases.service.crawler.ReferenceCasePageCrawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrawlerService {


    @Autowired
    private ExecuteCaseCrawler executeCaseCrawler;
    @Autowired
    private GuideCasePageCrawler guideCasePageCrawler;
    @Autowired
    private ReferenceCasePageCrawler referenceCasePageCrawler;
    @Autowired
    private CourtNoticeCrawler courtNoticeCrawler;

    public void startCrawler() {
//        executeCaseCrawler.crawler();
//        guideCasePageCrawler.crawler();
//        referenceCasePageCrawler.crawler();
        courtNoticeCrawler.crawler();
    }

}
