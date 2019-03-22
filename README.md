# 简介

### 代码简介
![](imgs/代码功能.png)

### 主要技术
- SSM
- mysql
- [Mybatis通用Mapper](https://gitee.com/free/Mapper)
- [okhttp](https://square.github.io/okhttp/)
- [jsoup](https://jsoup.org/)

### 整体逻辑
1. 使用爬虫爬取[中国审判流程信息公开网](https://splcgk.court.gov.cn/gzfwww//qwal)指导性案例与参考性案例列表与详情数据
2. 使用爬虫爬取[中国执行信息公开网](http://zxgk.court.gov.cn/)执行案例列表与详情数据
3. 对爬取到的数据进行提取，清洗，处理后入库
4. 对网站数据与数据库中数据根据时间与唯一标示进行比对，对已经在库中存在的数据不再进行抓取，仅抓取新增内容
5. 在项目启动时，执行1-4逻辑
6. 使用cron表达式，定时执行1-4逻辑