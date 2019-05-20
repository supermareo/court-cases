package com.court.cases.service;

import com.court.cases.model.ProxyResult;
import com.court.cases.utils.JsonUtil;
import com.court.cases.utils.OkHttpUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 使用代理
 */
@Service
public class ProxyService {

    @Value("${proxy.url}")
    private String proxyUrl;

    private ProxyResult.Proxy curProxy = null;

    //获取代理
    public ProxyResult.Proxy getProxy() {
        if (curProxy != null) {
            return curProxy;
        }
        String response = OkHttpUtil.get(proxyUrl);
        ProxyResult proxyResult = JsonUtil.fromJson(response, ProxyResult.class);
        if (proxyResult == null || !proxyResult.isSuccess()) {
            throw new RuntimeException("代理获取失败:" + response);
        }
        curProxy = proxyResult.getData().get(0);
        return curProxy;
    }

    //刷新代理
    public ProxyResult.Proxy refresh() {
        this.curProxy = null;
        return getProxy();
    }

}
