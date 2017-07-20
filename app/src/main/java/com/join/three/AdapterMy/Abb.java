package com.join.three.AdapterMy;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Abb extends Activity {
    private Map<String, List<Map<String, List<Map<String, String>>>>> aa = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 10; i++) {
            Map<String,String> map=new HashMap<>();
            map.put("a"+i,"aa"+i);
            List<Map<String, String>> map1=new ArrayList<>();
            map1.add(map);
            Map<String, List<Map<String, String>>> map2=new HashMap<>();
            map2.put("b"+i,map1);
            List<Map<String, List<Map<String, String>>>> maps3=new ArrayList<>();
            maps3.add(map2);
            aa.put("aa",maps3);
        }





    }


}
