package com.github.yuanqunwang.dbgen4j.table;

import com.github.javafaker.Faker;

import java.util.*;

public class TableFactory {
    private final Faker faker;
    private final TableSeedBundle tableSeedBundle;

    public TableFactory(){
        this(new Faker());
    }

    /**
     * constructor for
     * @param faker
     */
    public TableFactory(Faker faker){
        this.faker = faker;
        this.tableSeedBundle = null;
    }

    /**
     * constructor for multi tables having relation to each other
     * @param faker
     * @param tableSeedBundle
     */
    public TableFactory(Faker faker, TableSeedBundle tableSeedBundle){
        this.faker = faker;
        this.tableSeedBundle = tableSeedBundle;
    }

    /**
     * Constructor for single table
     * @param faker
     * @param tableSeed
     */
    public TableFactory(Faker faker, TableSeed tableSeed, int recordNum){
        this.faker = faker;
        this.tableSeedBundle = new TableSeedBundle(recordNum);
        this.tableSeedBundle.add(tableSeed);
    }

    public Table createTable(TableSeed tableSeed, int recordNum){
        List<ArrayList<String>> records = fakeMultiRecords(tableSeed, recordNum);
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
        List<ArrayList<String>> records = fakeMultiRecords(new ArrayList<String>(fieldAndDirective.values()), recordNum);
        return new Table(tableName, new ArrayList<String>(fieldAndDirective.keySet()), records);
    }

    /**
     * create multi tables having relation to each other
     * and fill each table with certain recordNum.
     * @return
     */
    public List<Table> createTableBundle(){
        List<Table> tableList = createTables();
        fillTablesWithMultiRecords(tableList);
        return tableList;
    }

    private List<Table> createTables(){
        int tableNum = tableSeedBundle.size();
        List<Table> tableList = new ArrayList<Table>(tableNum);

        for(int i = 0; i < tableNum; i++){
            TableSeed tableSeed = tableSeedBundle.get(i);
            String tableName = tableSeed.getTableName();
            List<String> fields = new ArrayList<String>(tableSeed.getFields());
            Table table = new Table(tableName, fields);
            tableList.add(table);
        }
        return tableList;
    }


    private void fillTablesWithMultiRecords(List<Table> tableList){
        int recordLen = tableSeedBundle.getRecordNum();
        for(int i = 0; i < recordLen; i++){
            List<TableSeed> tableSeedList = tableSeedBundle.nextTableSeedBundle(this);
            fillTablesWithSingleRecord(tableList, tableSeedList);
        }
    }

    private void fillTablesWithSingleRecord(List<Table> tableList, List<TableSeed> tableSeedList){
        int tableNum = tableList.size();
        for(int i = 0; i < tableNum; i++){
            Table table = tableList.get(i);
            TableSeed tableSeed = tableSeedList.get(i);
            List<String> directives = new ArrayList<String>(tableSeed.getFieldAndDirective(this).values());
            ArrayList<String> singleRecord = fakeSingleRecord(directives);
            table.insert(singleRecord);
        }
    }





    private List<ArrayList<String>> fakeMultiRecords(List<String> directives, int recordNum){
        List<ArrayList<String>> db = new ArrayList<ArrayList<String>>(recordNum);
        for(int i = 0; i < recordNum; i++){
            db.add(fakeSingleRecord(directives));
        }
        return db;
    }

    private List<ArrayList<String>> fakeMultiRecords(TableSeed tableSeed, int recordNum){
        List<ArrayList<String>> db = new ArrayList<ArrayList<String>>(recordNum);
        for(int i = 0; i < recordNum; i++){
            List<String> directives = new ArrayList<String>(tableSeed.getFieldAndDirective(this).values());
            db.add(fakeSingleRecord(directives));
        }
        return db;
    }

    /**
     * generate a single fake record as map
     * @param fieldAndDirective
     * @return generated fake record as map
     */
    public Map<String,String> createSingleRecord(Map<String, String> fieldAndDirective){
        Map<String, String> singleRecord = new HashMap<String, String>(fieldAndDirective);
        List<String> recordValue = fakeSingleRecord(new ArrayList<String>(fieldAndDirective.values()));
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
