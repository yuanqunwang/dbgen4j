package com.github.yuanqunwang.dbgen4j.seed;

import com.github.yuanqunwang.dbgen4j.utils.YamlUtil;
import org.junit.Test;

import java.util.Map;

public class YamlUtilTest {
    @Test
    public void loadYamlAsMap(){
        Map<String, Object> wlck = YamlUtil.loadValues("wlck.yml");
        for(String key : wlck.keySet()){
            Object o = wlck.get(key);
            if(o != null){
                System.out.println(key);
                System.out.println(wlck.get(key));
            }
        }
    }
}
