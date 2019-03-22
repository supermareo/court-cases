package com.court.cases.utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                .build();
    }

    public static String get(String url) {
        try {
            Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String post(String url, Map<String, Object> data, String contentType) {
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), GSON.toJson(data));
            Response response = client.newCall(new Request.Builder().url(url).post(requestBody).build()).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
        }
    }

}
