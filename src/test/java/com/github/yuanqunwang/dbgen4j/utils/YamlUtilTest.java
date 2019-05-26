package com.github.yuanqunwang.dbgen4j.utils;

import org.junit.Test;

import java.util.Map;

public class YamlUtilTest {
    @Test
    public void loadYamlAsMap(){
        Map<String, Object> rock = YamlUtil.loadValues("seed/rock.yml");
        for(String key : rock.keySet()){
            Object o = rock.get(key);
            if(o != null){
                System.out.println(key);
                System.out.println(rock.get(key));
            }
        }
    }
}
