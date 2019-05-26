package com.github.yuanqunwang.dbgen4j.utils;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeSeed;

import java.util.*;

/**
 * use java-faker to generate fake values.
 */



public class FakerUtil {
    private static Faker faker;


    /**
     * init Faker instance
     * and add custom defined fake object to it.
     */
    static {
        Map<String, Object> fakerConfig = YamlUtil.loadValues("faker-config.yml");
        String language = (String) fakerConfig.get("language");
        if(language == null){
            language = "en-US";
        }
        faker = new Faker(new Locale(language));

        Map<String, String> customDefined = (Map<String, String>)fakerConfig.get("custom_defined");
        Set<String> clazzs = customDefined.keySet();
        for(String clazz : clazzs){
            String seedYml = customDefined.get(clazz);
            try {
                Class<?> c = Class.forName(clazz);
                FakeSeed fakeSeed = new FakeSeed(seedYml, c);
                faker.addFakeSeed(fakeSeed);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * generate a single fake record as map
     * @param fieldAndDirective
     * @return generated fake record as map
     */
    public static Map<String,String> genFakeKeyValueMap(Map<String, String> fieldAndDirective){
        Map<String, String> singleRecord = new HashMap<String, String>(fieldAndDirective);
        List<String> recordValue = genFakeValueList(new ArrayList<String>(fieldAndDirective.values()));
        Set<String> fields = fieldAndDirective.keySet();
        int i = 0;
        for(String field : fields){
            singleRecord.put(field, recordValue.get(i++));
        }
        return singleRecord;
    }

    /**
     * fillTablesWithMultiRecords a list of fake record
     * @param directives
     * @return
     */
    public static ArrayList<String> genFakeValueList(List<String> directives){
        int directiveNum = directives.size();
        ArrayList<String> l = new ArrayList<String>(directiveNum);
        for(String directive : directives){
            String resolved = faker.expression(directive);
            String result = resolved == null ? "" : resolved;
            l.add(result);
        }
        return l;
    }
}
