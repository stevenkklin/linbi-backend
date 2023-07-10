package com.yupi.springbootinit.api;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Steven
 * @create 2023-07-04-15:00
 */
public class OpenAiApi {

    public static void main(String[] args) {

        String url = "https://api.openai.com/v1/completions";
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "请帮我分析");
        String json = JSONUtil.toJsonStr(hashMap);
        String result = HttpRequest.post(url)
                .header("Authorization", "Bearer 替换为自己的key")
                .body(json)
                .execute()
                .body();



    }


}
