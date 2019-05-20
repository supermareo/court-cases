package com.court.cases.model;

import lombok.Data;

import java.util.List;

@Data
public class ProxyResult {

    private int code;
    private boolean success;
    private String msg;
    private List<Proxy> data;

    @Data
    public static class Proxy {
        private String ip;
        private int port;
        private String expire_time;
        private String city;
        private String isp;
    }

}
