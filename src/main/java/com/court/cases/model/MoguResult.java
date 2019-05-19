package com.court.cases.model;

import lombok.Data;

import java.util.List;

@Data
public class MoguResult {

    private int code;
    private List<Proxy> msg;

    @Data
    public static class Proxy {
        private String ip;
        private String port;
    }

}
