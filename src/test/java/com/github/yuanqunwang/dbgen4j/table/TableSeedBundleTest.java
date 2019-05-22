package com.github.yuanqunwang.dbgen4j.table;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeSeed;
import com.github.yuanqunwang.dbgen4j.seed.Sfck;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TableSeedBundleTest {
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
    public void initFildsAnddirectives(){
        fieldsAndDirectives.put("book", "#{Book.title}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("app", "#{app.name}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("university", "#{university.name}-#{phoneNumber.cellPhone}");
        fieldsAndDirectives.put("cxlx", "#{sfck.cxlx}-#{phoneNumber.cellPhone}");
    }

    @Test
    public void tableSeedBundleTest(){
        List<TableSeed> tableSeedList = new ArrayList<TableSeed>(4);
        TableSeed tableSeed1 = new TableSeed("tableSeed1", fieldsAndDirectives);
        fieldsAndDirectives.put("file","#{File.extension}");
        TableSeed tableSeed2 = new TableSeed("tableSeed2", fieldsAndDirectives);
        fieldsAndDirectives.put("bank_code", "#{sfck.bank_code}");
        TableSeed tableSeed3 = new TableSeed("tableSeed3", fieldsAndDirectives);
        fieldsAndDirectives.put("id_type", "#{sfck.id_type}");
        fieldsAndDirectives.put("org_code", "#{sfck.org_code}");
        TableSeed tableSeed4 = new TableSeed("tableSeed4", fieldsAndDirectives);
        tableSeedList.add(tableSeed1);
        tableSeedList.add(tableSeed2);
        tableSeedList.add(tableSeed3);
        tableSeedList.add(tableSeed4);
        TableSeedBundle tableSeedBundle = new TableSeedBundle(tableSeedList, 5);
        for(int i = 0; i < 5; i++){
            List<TableSeed> tableSeedList1 = tableSeedBundle.nextTableSeedBundle(tableFactory);
            for(TableSeed tableSeed : tableSeedList1){
                System.out.println(tableSeed.nextDirective(tableFactory));
            }
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }

    @Test
    public void stringUtilsTest(){
        String toBeReplace = "test book john is good book";
        String search = "book";
        String replacement = "done";
        String result = StringUtils.replaceAll(toBeReplace, search, replacement);
        assert(!result.contains(search));
        System.out.println(result);
    }
}
