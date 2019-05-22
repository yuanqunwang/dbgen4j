package com.github.yuanqunwang.dbgen4j.utils;

import com.github.yuanqunwang.dbgen4j.utils.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapUtilTest {
    Map<String, String> fieldsAndDirectives = new HashMap<String, String>();

    @Test
    public void test(){
        fieldsAndDirectives.put("book", "#{Book.title}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("app", "#{app.name}-#{phoneNumber.cellPhone}-#{Name.name}+++++@{book}");
        fieldsAndDirectives.put("university", "#{university.name}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("name","@{app}");
        fieldsAndDirectives.put("phoneNumber", "#{phoneNumber.cellPhone}");
        MapUtil.resolveReference(fieldsAndDirectives);
        String app = fieldsAndDirectives.get("app");
        String name = fieldsAndDirectives.get("name");
        String book = fieldsAndDirectives.get("book");
        assert(app.contains(book));
        assert(app.equals(name));
        System.out.println(fieldsAndDirectives);

    }


}
