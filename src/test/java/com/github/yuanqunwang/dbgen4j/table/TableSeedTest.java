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
        FakeSeed fakeSeed = new FakeSeed("seed/sfck.yml", Sfck.class);
        faker.addFakeSeed(fakeSeed);
        tableFactory = new TableFactory(faker);
    }
    @Before
    public void initFieldsAndDirectives(){
        fieldsAndDirectives.put("cellphone","#{PhoneNumber.cellPhone}");
        fieldsAndDirectives.put("name", "#{Name.name}");

        fieldsAndDirectives.put("book", "#{Book.title}-#{PhoneNumber.cellPhone}#{Name.name}test");
        fieldsAndDirectives.put("app", "#{App.name}-#{PhoneNumber.cellPhone}");
        fieldsAndDirectives.put("university", "#{PhoneNumber.cellPhone}--uni--#{University.name}");
    }
    @Test
    public void tableSeedTest(){
        TableSeed tableSeed = new TableSeed("testTableSeed", fieldsAndDirectives);
        for(int i = 0; i < 1000; i++){
            Map<String, String> fieldAndDirective = tableSeed.nextFieldAndDirective(tableFactory);
            String book = fieldAndDirective.get("book");
            String name = fieldAndDirective.get("name");
            String app = fieldAndDirective.get("app");
            String university = fieldAndDirective.get("university");
            String cellphone = fieldAndDirective.get("cellphone");
            assert(book.contains(name));
            assert (app.contains(cellphone));
            assert(university.contains(cellphone));
//            assert (university.endsWith(name));
            System.out.println(fieldAndDirective);
            System.out.println();
        }
    }
}
