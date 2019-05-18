package com.github.yuanqunwang.dbgen4j.seed;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeSeed;
import com.github.yuanqunwang.dbgen4j.table.Table;
import com.github.yuanqunwang.dbgen4j.table.TableFactory;
import com.github.yuanqunwang.dbgen4j.table.TableSeed;
import com.github.yuanqunwang.dbgen4j.utils.YamlUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SfckTest {
    private Faker faker;
    private Table table;
    @Before
    public void before(){
        faker = new Faker(new Locale("zh-CN"));
        FakeSeed fakeSeed = new FakeSeed("sfck", Sfck.class);
        faker.addFakeSeed(fakeSeed);
        getTable();
    }

    @Test
    public void getTable(){
        Map<String, String> fieldsAndDirectives = new LinkedHashMap<String, String>();
//        fieldsAndDirectives.put("city", "#{address.city}");
        fieldsAndDirectives.put("cell_phone", "#{phoneNumber.cellPhone}");
//        fieldsAndDirectives.put("name", "#{Name.name}");
        fieldsAndDirectives.put("book", "#{Book.title}-#{phoneNumber.cellPhone}-#{phoneNumber.cellPhone}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("app", "#{app.name}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("university", "#{university.name}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("cxlx", "#{sfck.cxlx}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("id_type", "#{sfck.id_type}");
        fieldsAndDirectives.put("org_code", "#{sfck.org_code}");
        fieldsAndDirectives.put("bank_code", "#{sfck.bank_code}");
        fieldsAndDirectives.put("file","#{File.extension}");
//        fieldsAndDirectives.put("job","#{Job.title}");
//        fieldsAndDirectives.put("dateandtime","#{DateAndTime.between '20180401','20190516'}");
        fieldsAndDirectives.put("dateAndTime", "#{dateAndTime.dateAndTime 'yyyy年MM月dd日-HH:mm:ss'}");

//        DBGen dbGen = new DBGen(seed, fieldsAndDirectives);

        TableFactory tableFactory = new TableFactory(faker);
        TableSeed tableSeed = new TableSeed("testTable", fieldsAndDirectives);

        this.table = tableFactory.createTable(tableSeed, 300);
    }


    @Test
    public void testDBGen(){
        System.out.println("before update:");
        System.out.println(table.toString());
        System.out.println();
        System.out.println();

        List<String> field = table.getFields();
        List<? extends List<String>> records = table.getRecords();
        List<String> record3 = records.get(2);
        Map<String, String> updateInfo = new LinkedHashMap<String, String>(field.size());
        int i = 0;
        for(String f : field){
            updateInfo.put(f, record3.get(i++));
        }

        System.out.println(updateInfo);

        table.update(0, updateInfo);
        List<String> record1 = records.get(0);


        i = 0;
        assertEquals(record1.size(), record3.size());
        for(String s1 : record1){
            assertEquals(s1, record3.get(i++));
        }

        System.out.println("after update:");
        System.out.println(table.toString());
        System.out.println();
    }

    @Test
    public void loadYamlAsMap(){
        System.out.println("before update:");
        System.out.println(table.toString());

        Map<String, Object> wlck = YamlUtil.loadValues("message.yml");
        int i = 0;
        for(String key : wlck.keySet()){
            Object o = wlck.get(key);
            if(o != null){
                System.out.println(key);
                System.out.println(wlck.get(key));

                for(Map m : (List<Map>) o){
                    table.update(i++, m);
                }
            }
        }
        System.out.println("after update:");
        System.out.println(table.toString());
    }


}
