package com.willi.bean;

import com.alibaba.fastjson.JSON;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-22 20:56
 **/

public class Test {
    public static void main(String[] args) {
        Neigh neigh1 = new Neigh(117, "东城区");
        String s = JSON.toJSONString(neigh1);
        System.out.println(s);
    }
}
