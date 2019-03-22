package com.court.cases.scheduler;

import com.court.cases.service.CrawlerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SchedulerTask {

    @Autowired
    private CrawlerService crawlerService;

    @Scheduled(cron = "${cron.crawler}")
    public void init() {
        log.info("定时任务开启");
        crawlerService.startCrawler();
        log.info("定时任务结束");
    }

}
