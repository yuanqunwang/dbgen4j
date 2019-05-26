package com.github.yuanqunwang.dbgen4j.table;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TableFactoryTest {
    private TableFactory tableFactory;

    @Before
    public void initFieldsAddictive(){
        Map<String, String> fieldsAndDirectives = new LinkedHashMap<String, String>();
        fieldsAndDirectives.put("book", "#{Book.title}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("app", "#{app.name}-#{phoneNumber.cellPhone}-@{name}");
        fieldsAndDirectives.put("university", "#{university.name}-@{phoneNumber}");
        fieldsAndDirectives.put("name","#{Name.name}");
        fieldsAndDirectives.put("phoneNumber", "#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("rock", "#{Rock.rock}");
        fieldsAndDirectives.put("Sedimentary rock", "#{Rock.full_desc}");
        tableFactory = new TableFactory("FactoryTest", fieldsAndDirectives);
    }

    @Test
    public void tableFactoryTest(){
        int recordLen = 10;
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

        Map<String, String> fieldsAndDirectives = new LinkedHashMap<String, String>();
        fieldsAndDirectives.put("name", "John");
        fieldsAndDirectives.put("app", "angry bird");
        table.updateRecord(0, fieldsAndDirectives);
        table.updateRecord(1, fieldsAndDirectives);
        table.updateRecord(2, fieldsAndDirectives);
        table.updateRecord(3, fieldsAndDirectives);
        table.updateRecord(9, fieldsAndDirectives);
        System.out.println(table.toString());

    }

}
