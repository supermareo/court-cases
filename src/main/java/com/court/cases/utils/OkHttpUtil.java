package com.court.cases.utils;

import com.court.cases.model.MoguResult;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static OkHttpClient client;
    private static final Gson GSON = new Gson();

    private static Map<String, List<Cookie>> cookieMap = new HashMap<>();

    static {
        client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieMap.put(url.host(), cookies);
                    }

                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieMap.getOrDefault(url.host(), Collections.emptyList());
                        System.out.println();
                        return cookies;
                    }
                })
                .readTimeout(30, TimeUnit.SECONDS) //读取超时
                .writeTimeout(30, TimeUnit.SECONDS) //写超时
                .build();
    }

    public static String get(String url) {
        try {
            Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    public static String post(String url, Map<String, Object> data, String contentType) {
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), GSON.toJson(data));
            Response response = client.newCall(new Request.Builder().url(url).post(requestBody).build()).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    public static String postForm(String url, Map<String, Object> data) {
        try {
            FormBody.Builder builder = new FormBody.Builder();
            data.forEach((s, o) -> builder.add(s, (String) o));
            FormBody formBody = builder.build();
            Response response = client.newCall(new Request.Builder().url(url).post(formBody).build()).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 带重试的请求
     */
    public static String postForm(String url, Map<String, Object> data, int retry) {
        if (retry > 0) {
            String s = postForm(url, data);
            if (s == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                    e.printStackTrace();
                }
                return postForm(url, data, retry - 1);
            }
            return s;
        }
        return null;
    }

    /**
     * 带代理的请求
     */
    public static String postForm(String url, Map<String, Object> data, MoguResult.Proxy proxy) {
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            Proxy p = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy.getIp(), Integer.parseInt(proxy.getPort())));
            builder.proxy(p);

            FormBody.Builder fb = new FormBody.Builder();
            data.forEach((s, o) -> fb.add(s, (String) o));
            FormBody formBody = fb.build();
            Response response = builder.build().newCall(new Request.Builder().url(url).post(formBody).build()).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

}
