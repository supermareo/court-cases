package com.court.cases;

import com.court.cases.service.CrawlerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class CasesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasesApplication.class, args);
    }

    @Autowired
    private CrawlerService crawlerService;

    @PostConstruct
    public void init() {
        log.info("启动时执行开始");
        new Thread(() -> crawlerService.startCrawler()).start();
        log.info("启动时执行结束");
    }

}
