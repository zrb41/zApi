package com.zrb.zapisdk;

// 客户端

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.zrb.zapicommon.model.dto.InterfaceInfoInvokeRequest;
import com.zrb.zapisdk.utils.SignUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class ZApiClient {

    private String accessKey;

    private String secretKey;

    private static final String GATEWAY_HOST="http://localhost:8090";

    // 生成请求头
    private Map<String,String> getHeaderMap(String userRequestParams){
        HashMap<String, String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        // todo 用Redis
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("userRequestParams",userRequestParams);
        map.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        map.put("sign", SignUtils.genSign(userRequestParams,secretKey));
        return map;
    }

    // 参数一：接口id
    // 参数二：用户参数
    public String redirect(Long interfaceInfoId,String userRequestParams){

        String id = String.valueOf(interfaceInfoId);
        String url=GATEWAY_HOST + "/api/demo/";
        String name = switch (id) {
            case "1" -> "getSum";
            case "2" -> "getMax";
            case "3" -> "sort";
            case "4" -> "hello";
            default -> "noSuchMethod";
        };
        url+=name;
        // 给网关发送post请求
        HttpResponse httpResponse = HttpRequest.post(url)
                .addHeaders(getHeaderMap(userRequestParams))
                .body(userRequestParams)
                .execute();
        // 返回响应结果
        return httpResponse.body();
    }

}
