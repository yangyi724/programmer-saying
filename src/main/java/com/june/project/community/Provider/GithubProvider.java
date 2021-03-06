package com.june.project.community.Provider;

import com.alibaba.fastjson.JSON;
import com.june.project.community.dto.AccessTokenDTO;
import com.june.project.community.dto.GithubUser;
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author June
 * @date 2020/6/30 - 15:18
 */
@Component
public class GithubProvider {
//    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
//        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
//        Request request = new Request.Builder()
//                .url("https://github.com/login/oauth/access_token")
//                .post(body)
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            String string = response.body().string();
//            String token = string.split("&")[0].split("=")[1];
//            return token;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        int timeout = 60;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout * 1000)
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .build();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://github.com/login/oauth/access_token");
        httpPost.setConfig(defaultRequestConfig);
        httpPost.setHeader("Content-Type", "application/json");
        try {
            httpPost.setEntity(new StringEntity(JSON.toJSONString(accessTokenDTO), ContentType.create("application/json", "utf-8")));
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                String token = result.split("&")[0].split("=")[1];
                return token;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

//    public GithubUser getUser(String accessToken) {
//        // RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout * 1000).setConnectTimeout(timeout * 1000).setConnectionRequestTimeout(timeout * 1000).build();
//        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(60000, TimeUnit.MILLISECONDS)
//                .readTimeout(60000, TimeUnit.MILLISECONDS)
//                .build();
////        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("https://api.github.com/user?access_token=" + accessToken)
//                .build();
//
//        try {
//            Response response = client.newCall(request).execute();
//            String string = response.body().string();
//            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
//            return githubUser;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public GithubUser getUser(String accessToken) throws Exception {
        int timeout = 60;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout * 1000)
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("https://api.github.com/user");
        httpget.addHeader(new BasicHeader("Authorization", "token "+accessToken));
        httpget.setProtocolVersion(HttpVersion.HTTP_1_0);
        httpget.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        httpget.setConfig(defaultRequestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            GithubUser user = JSON.parseObject(entity.getContent(),GithubUser.class);
            EntityUtils.consume(entity);
            response.close();
            return user;
        } catch (Exception e){
            throw e;
        } finally{
            if(response != null){
                response.close();
            }
            if(httpclient!=null){
                httpclient.close();
            }
        }
    }

}
