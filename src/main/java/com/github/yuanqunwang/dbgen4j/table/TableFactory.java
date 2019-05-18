package com.github.yuanqunwang.dbgen4j.table;

import com.github.javafaker.Faker;

import java.util.*;

public class TableFactory {
    private final Faker faker;

    public TableFactory(){
        this(new Faker());
    }
    public TableFactory(Faker faker){
        this.faker = faker;
    }

    public Table createTable(TableSeed tableSeed, int recordNum){
        List<? extends List<String>> records = fakeMultiRecords(tableSeed, recordNum);
        return new Table(tableSeed.getTableName(), new ArrayList<String>(tableSeed.getFieldAndDirective(this).keySet()), records);
    }

    /**
     * only called by {@Link TableSeed}, call above one to make sure generate different field.
     * @param tableName {@Link String}
     * @param fieldAndDirective
     * @param recordNum
     * @return
     */
    public Table createTable(String tableName, Map<String, String> fieldAndDirective, int recordNum){
        List<? extends List<String>> records = fakeMultiRecords(new ArrayList<String>(fieldAndDirective.values()), recordNum);
        return new Table(tableName, new ArrayList<String>(fieldAndDirective.keySet()), records);
    }

    private List<? extends List<String>> fakeMultiRecords(List<String> directives, int recordNum){
        List<ArrayList<String>> db = new ArrayList<ArrayList<String>>(recordNum);
        for(int i = 0; i < recordNum; i++){
            db.add(fakeSingleRecord(directives));
        }
        return db;
    }

    private List<? extends List<String>> fakeMultiRecords(TableSeed tableSeed, int recordNum){
        List<ArrayList<String>> db = new ArrayList<ArrayList<String>>(recordNum);
        for(int i = 0; i < recordNum; i++){
            List<String> directives = new ArrayList<String>(tableSeed.getFieldAndDirective(this).values());
            db.add(fakeSingleRecord(directives));
        }
        return db;
    }

    private ArrayList<String> fakeSingleRecord(List<String> directives){
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
