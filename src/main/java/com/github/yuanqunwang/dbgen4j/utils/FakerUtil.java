package com.github.yuanqunwang.dbgen4j.utils;

import com.github.javafaker.Faker;

import java.util.*;

public class FakerUtil {
    private static Faker faker;
    static {
        faker = new Faker(new Locale("zh-CN"));
//        FakeSeed fakeSeed = new FakeSeed("seed/sfck.yml", Sfck.class);
//        faker.addFakeSeed(fakeSeed);
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
