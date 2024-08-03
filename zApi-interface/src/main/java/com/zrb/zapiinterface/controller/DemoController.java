package com.zrb.zapiinterface.controller;

import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("demo")
public class DemoController {

    // 对数组求和
    @PostMapping("getSum")
    public String getSum(@RequestBody String userRequestParams) {
        // 将json字符串"[1,2,3]”解析为int数组
        Gson gson = new Gson();
        int[] numbers = gson.fromJson(userRequestParams, int[].class);
        int sum=0;
        for (int number : numbers) {
            sum+=number;
        }
        return String.valueOf(sum);
    }

    // 寻找数组中的最大值
    @PostMapping("getMax")
    public String getMax(@RequestBody String userRequestParams){
        // 将json字符串"[1,2,3]”解析为int数组
        Gson gson = new Gson();
        int[] numbers = gson.fromJson(userRequestParams, int[].class);
        int maxNum=numbers[0];
        for (int number : numbers) {
            if(number>maxNum){
                maxNum=number;
            }
        }
        return String.valueOf(maxNum);
    }

    // 给数组排序
    @PostMapping("sort")
    public String sort(@RequestBody String userRequestParams){
        // 将json字符串"[1,2,3]”解析为int数组
        Gson gson = new Gson();
        int[] numbers = gson.fromJson(userRequestParams, int[].class);
        Arrays.sort(numbers);
        return Arrays.toString(numbers);
    }

    @PostMapping("hello")
    public String hello(@RequestBody String userRequestParams){
        return "hello "+userRequestParams+"_=";
    }

    @PostMapping("noSuchMethod")
    public String noSuchMethod(@RequestBody String userRequestParams){
        return "noSuchMethod";
    }

}
