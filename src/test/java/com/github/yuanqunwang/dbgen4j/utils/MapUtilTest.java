package com.github.yuanqunwang.dbgen4j.utils;

import com.github.yuanqunwang.dbgen4j.utils.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.Style;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapUtilTest {
    Map<String, String> fieldsAndDirectives = new LinkedHashMap<String, String>();
    @Before
    public void init(){
        fieldsAndDirectives.put("book", "#{Book.title}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("app", "#{app.name}-#{phoneNumber.cellPhone}-#{Name.name}+++++@{book}");
        fieldsAndDirectives.put("university", "#{university.name}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("name","@{app}");
        fieldsAndDirectives.put("phoneNumber", "#{phoneNumber.cellPhone}");
    }

    @Test
    public void test(){
        MapUtil.resolveReference(fieldsAndDirectives);
        String app = fieldsAndDirectives.get("app");
        String name = fieldsAndDirectives.get("name");
        String book = fieldsAndDirectives.get("book");
        assert(app.contains(book));
        assert(app.equals(name));
        System.out.println(fieldsAndDirectives);
    }

    @Test
    public void testList(){
        List<String> list1 = new ArrayList<String>();
        list1.add("good1");
        list1.add("good2");
        list1.add("good3");
        list1.add("good4");
        list1.add("good5");
        List<String> list2 = new ArrayList<String>(list1);

        list2.set(0, "good1");
        int size = list1.size();
        for(int i = 0; i < size; i++){
            System.out.println(list1.get(i) == list2.get(i));
        }
        System.out.println(list1);
    }


    @Test
    public void testMap(){
        Map<String, String> mapCopy = new LinkedHashMap<String, String>(fieldsAndDirectives);
        mapCopy.put("book", "put from mapCopy");
        String mapBook = fieldsAndDirectives.get("book");
        String mapCopyBook = mapCopy.get("book");
        System.out.println(fieldsAndDirectives);
        System.out.println(mapCopy);
        assert(mapBook.equals(mapCopyBook));
    }


    @Test
    public void testMergeMap(){
        String[] key = {"app","phoneNumber","university","book","name"};
        String[] value = {"app","phoneNumber","university","book","name"};

        List<String> keyList = Arrays.asList(key);
        List<String> valueList = Arrays.asList(value);
        Map<String, String> map = MapUtil.mergeMap(Arrays.asList(key), Arrays.asList(value));



        valueList.set(0, "setFromList");
        value[0] = "setFromArray";
        System.out.println(Arrays.asList(key));
        System.out.println(Arrays.asList(value));
        System.out.println(map);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();





        map.put("app", "setFromMap");

        int i = 0;
        System.out.println("Key:");
        for(String s : map.keySet()){
            System.out.println(s == keyList.get(i++));
        }


        i = 0;
        System.out.println("Values:");
        for(String s : map.values()){
            System.out.println(s == valueList.get(i++));
        }



        System.out.println(map);





    }


}
