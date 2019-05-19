package com.court.cases.service;

import com.court.cases.model.MoguResult;
import com.court.cases.utils.JsonUtil;
import com.court.cases.utils.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 使用蘑菇代理
 */
@Service
public class ProxyService {

    @Value("${url.mogu}")
    private String moguUrl;

    private MoguResult.Proxy curProxy = null;

    //获取代理
    public MoguResult.Proxy getProxy() {
        if (curProxy != null) {
            return curProxy;
        }
        String response = OkHttpUtil.get(moguUrl);
        MoguResult moguResult = JsonUtil.fromJson(response, MoguResult.class);
        if (moguResult == null) {
            throw new RuntimeException("蘑菇代理获取失败:" + response);
        }
        curProxy = moguResult.getMsg().get(0);
        return curProxy;
    }

    //刷新代理
    public MoguResult.Proxy refresh() {
        this.curProxy = null;
        return getProxy();
    }

}
