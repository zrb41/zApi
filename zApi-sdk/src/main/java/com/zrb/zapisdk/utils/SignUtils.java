package com.zrb.zapisdk.utils;

import cn.hutool.crypto.digest.DigestUtil;

public class SignUtils {

    // 根据userRequestParams和secretKey生成签名
    public static String genSign(String userRequestParams, String secretKey) {
        return DigestUtil.md5Hex(userRequestParams + "." + secretKey);
    }

}
