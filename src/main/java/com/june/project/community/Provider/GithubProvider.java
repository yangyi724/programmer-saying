package com.june.project.community.Provider;

import com.alibaba.fastjson.JSON;
import com.june.project.community.dto.AccessTokenDTO;
import com.june.project.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author June
 * @date 2020/6/30 - 15:18
 */
@Component
public class GithubProvider {
    /*
     * 3. 携带 accessTokenDTO 中的 code 信息(首先要把信息从Java类型转化成JSON类型)
     *    向 Github 的 access_token API (https://github.com/login/oauth/access_token) 发出 POST，获取 AccessToken
     *    access_token=e72e16c7e42f292c6912e7710c838347ae178b4a&token_type=bearer
     * 4. 获取到 access_token，返回 access_token
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 5. 携带 access_token Github 的 user API (https://api.github.com/user?access_token=) 发出 Request，获取用户信息
     * 6. 获取到JSON格式的用户信息，解析成Java类格式的用户信息 GithubUser
     */
    public GithubUser getUser(String accessToken) {
        // RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout * 1000).setConnectTimeout(timeout * 1000).setConnectionRequestTimeout(timeout * 1000).build();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
