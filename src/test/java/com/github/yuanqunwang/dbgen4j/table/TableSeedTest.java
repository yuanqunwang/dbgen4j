package com.github.yuanqunwang.dbgen4j.table;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeSeed;
import com.github.yuanqunwang.dbgen4j.seed.Sfck;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class TableSeedTest {
    private Faker faker;
    private TableFactory tableFactory;
    private Map<String, String> fieldsAndDirectives = new LinkedHashMap<String, String>();
    @Before
    public void initFakerAndTableFactory(){
        faker = new Faker(new Locale("zh-CN"));
        FakeSeed fakeSeed = new FakeSeed("sfck", Sfck.class);
        faker.addFakeSeed(fakeSeed);
        tableFactory = new TableFactory(faker);
    }
    @Before
    public void initFildsAnddirectives(){
        fieldsAndDirectives.put("cell_phone", "#{phoneNumber.cellPhone}--#{dateAndTime.dateAndTime 'yyyy年MM月dd日-HH:mm:ss'}");
        fieldsAndDirectives.put("book", "#{Book.title}-#{phoneNumber.cellPhone}-#{phoneNumber.cellPhone}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("app", "#{app.name}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("university", "#{university.name}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("cxlx", "#{sfck.cxlx}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("id_type", "#{sfck.id_type}");
        fieldsAndDirectives.put("org_code", "#{sfck.org_code}");
        fieldsAndDirectives.put("bank_code", "#{sfck.bank_code}");
        fieldsAndDirectives.put("file","#{File.extension}");
        fieldsAndDirectives.put("dateAndTime", "#{dateAndTime.dateAndTime 'yyyy年MM月dd日-HH:mm:ss'}");
    }
    @Test
    public void tableSeedTest(){
        TableSeed tableSeed = new TableSeed("testTableSeed", fieldsAndDirectives);
        System.out.println("before update:\n" + fieldsAndDirectives);
        System.out.println("after update:\n" + tableSeed.getFieldAndDirective(tableFactory));
    }
}
