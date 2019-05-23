package com.github.yuanqunwang.dbgen4j.table;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TableFactoryTest {
    private TableFactory tableFactory;

    @Before
    public void initFildsAnddirectives(){
        Map<String, String> fieldsAndDirectives = new LinkedHashMap<String, String>();
        fieldsAndDirectives.put("book", "#{Book.title}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("app", "#{app.name}-#{phoneNumber.cellPhone}-@{name}");
        fieldsAndDirectives.put("university", "#{university.name}-@{phoneNumber}");
        fieldsAndDirectives.put("name","#{Name.name}");
        fieldsAndDirectives.put("phoneNumber", "#{phoneNumber.cellPhone}");
        tableFactory = new TableFactory("FactoryTest", fieldsAndDirectives);
    }

    @Test
    public void tableFactoryTest(){
        int recordLen = 100000;
        Table table = tableFactory.createTable(recordLen);
        for(int i = 0; i < recordLen; i++){
           String app = table.getSpecificField(i, "app");
           String name = table.getSpecificField(i, "name");
           String book = table.getSpecificField(i, "book");
           String university = table.getSpecificField(i, "university");
           String phoneNumber = table.getSpecificField(i, "phoneNumber");
           assert(app.contains(name));
           assert(!book.contains(phoneNumber));
           assert(university.contains(phoneNumber));
        }
        System.out.println(table.toString());

    }

}
