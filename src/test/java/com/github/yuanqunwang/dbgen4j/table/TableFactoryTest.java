package com.github.yuanqunwang.dbgen4j.table;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeSeed;
import com.github.yuanqunwang.dbgen4j.seed.Sfck;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TableFactoryTest {
    private Faker faker;
    private TableFactory tableFactory;
    private Map<String, String> fieldsAndDirectives = new LinkedHashMap<String, String>();
    private TableSeedBundle tableSeedBundle;

    @Before
    public void initFakerAndTableFactory(){
        faker = new Faker(new Locale("zh-CN"));
        FakeSeed fakeSeed = new FakeSeed("seed/sfck.yml", Sfck.class);
        faker.addFakeSeed(fakeSeed);
        tableFactory = new TableFactory(faker);
    }
    @Before
    public void initFildsAnddirectives(){
        fieldsAndDirectives.put("book", "#{Book.title}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("app", "#{app.name}-#{phoneNumber.cellPhone}-#{Name.name}");
        fieldsAndDirectives.put("university", "#{university.name}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("name","#{Name.name}");
        fieldsAndDirectives.put("phoneNumber", "#{phoneNumber.cellPhone}");

    }

    @Before
    public void initTableSeedBundle(){
        List<TableSeed> tableSeedList = new ArrayList<TableSeed>(4);
        TableSeed tableSeed1 = new TableSeed("tableSeed1", fieldsAndDirectives);
        TableSeed tableSeed2 = new TableSeed("tableSeed2", fieldsAndDirectives);
        TableSeed tableSeed3 = new TableSeed("tableSeed3", fieldsAndDirectives);
        TableSeed tableSeed4 = new TableSeed("tableSeed4", fieldsAndDirectives);
        tableSeedList.add(tableSeed1);
        tableSeedList.add(tableSeed2);
        tableSeedList.add(tableSeed3);
        tableSeedList.add(tableSeed4);
        tableSeedBundle = new TableSeedBundle(tableSeedList, 5);
    }

    @Test
    public void sameDirectiveGenerateSameValue(){
        TableFactory tableFactory = new TableFactory(faker, tableSeedBundle);
        List<Table> tableList = tableFactory.createTableBundle();
        int tableNum = tableSeedBundle.size();
        int recordLen = tableSeedBundle.getRecordNum();
        for(int i = 0; i < recordLen; i++){
            Table firstTable = tableList.get(0);
            String firstName = firstTable.getSpecificField(i, "name");
            for(int j = 1; j < tableNum; j++){
                Table table = tableList.get(j);
                String app = table.getSpecificField(i, "app");
                assert(app.endsWith(firstName));
            }
        }
    }

    @Test
    public void sameFieldNotEqual(){
        TableFactory tableFactory = new TableFactory(faker, tableSeedBundle);
        List<Table> tableList = tableFactory.createTableBundle();
        Table table = tableList.get(0);
        int recordLen = tableSeedBundle.getRecordNum();
        String name = table.getSpecificField(0, "name");
        for(int i = 1; i < recordLen; i++){
            String name1 = table.getSpecificField(i, "name");
            assert(!name.equals(name1));
            name = name1;
        }

    }
}
