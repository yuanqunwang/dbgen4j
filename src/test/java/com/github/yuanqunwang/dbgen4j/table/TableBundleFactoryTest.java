package com.github.yuanqunwang.dbgen4j.table;

import org.junit.Before;
import org.junit.Test;
import sun.tools.jconsole.Tab;

import java.util.*;

public class TableBundleFactoryTest {
    private TableBundleFactory tableBundleFactory;

    @Before
    public void initFildsAnddirectives(){
        Map<String, String> fieldsAndDirectives = new LinkedHashMap<String, String>();
        fieldsAndDirectives.put("name","#{Name.name}");
        fieldsAndDirectives.put("phoneNumber", "#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("IdNumber", "#{IdNumber.valid}");

        Map<String, String> fd1 = new LinkedHashMap<String, String>(fieldsAndDirectives);
        Map<String, String> fd2 = new LinkedHashMap<String, String>(fieldsAndDirectives);
        Map<String, String> fd3 = new LinkedHashMap<String, String>(fieldsAndDirectives);

        fd1.put("book", "#{Book.title}-#{phoneNumber.cellPhone}");
        fd2.put("app", "#{app.name}-#{phoneNumber.cellPhone}-@{name}");
        fd3.put("university", "#{university.name}-@{phoneNumber}");

        Map<String, Map<String, String>> nameFieldDirective = new HashMap<String, Map<String, String>>(3);
        nameFieldDirective.put("table1", fd1);
        nameFieldDirective.put("table2", fd2);
        nameFieldDirective.put("table3", fd3);

        tableBundleFactory = new TableBundleFactory(nameFieldDirective);

    }

    @Test
    public void tableBundleFactoryTest(){
        int recordLen = 10;
        TableBundle tableBundle = tableBundleFactory.createTableBundle(recordLen);
        Table table1 = tableBundle.get(0);
        Table table2 = tableBundle.get(1);
        Table table3 = tableBundle.get(2);

        for(int i = 0; i < recordLen; i++){
            String tb1name = table1.getSpecificField(i, "name");
            String tb2name = table1.getSpecificField(i, "name");
            String tb3name = table1.getSpecificField(i, "name");
            assert(tb1name.equals(tb2name));
            assert(tb1name.equals(tb3name));

            String tb1phoneNumber = table1.getSpecificField(i, "phoneNumber");
            String tb2phoneNumber = table1.getSpecificField(i, "phoneNumber");
            String tb3phoneNumber = table1.getSpecificField(i, "phoneNumber");
            assert(tb1phoneNumber.equals(tb2phoneNumber));
            assert(tb1phoneNumber.equals(tb3phoneNumber));

            String tb1IdNumber = table1.getSpecificField(i, "IdNumber");
            String tb2IdNumber = table1.getSpecificField(i, "IdNumber");
            String tb3IdNumber = table1.getSpecificField(i, "IdNumber");
            assert(tb1IdNumber.equals(tb2IdNumber));
            assert(tb1IdNumber.equals(tb3IdNumber));

            System.out.println(table1.getSingleRecord(i));
            System.out.println(table2.getSingleRecord(i));
            System.out.println(table3.getSingleRecord(i));
            System.out.println();
            System.out.println();
        }

    }
}
