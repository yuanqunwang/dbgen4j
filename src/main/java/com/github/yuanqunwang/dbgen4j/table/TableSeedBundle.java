package com.github.yuanqunwang.dbgen4j.table;

import java.util.*;

/**
 * multi {@Link TableSeed} having relation to each other
 */
public class TableSeedBundle extends ArrayList<TableSeed> {
    private int recordNum;

    public TableSeedBundle(int recordNum){
        super();
        this.recordNum = recordNum;
    }

    public TableSeedBundle(List<TableSeed> tableSeedList, int recordNum){
        super(tableSeedList);
        this.recordNum = recordNum;
    }


    public List<TableSeed> getTableSeeds(){
        return this;
    }


    public List<TableSeed> nextTableSeedBundle(TableFactory tableFactory) {
        return populateCommonField(tableFactory);
    }

    private List<TableSeed> populateCommonField(TableFactory tableFactory){
        List<TableSeed> tableSeeds = new ArrayList<TableSeed>(size());
        for(TableSeed tableSeed : this){
            tableSeeds.add(new TableSeed(tableSeed.getTableName(),tableSeed.getFieldAndDirective(tableFactory)));
        }

        Map<String, String> commonFieldAndDirectives = getCommonFieldAndDirective(tableFactory, tableSeeds);
        Map<String, String> commonFieldAndValues = tableFactory.createSingleRecord(commonFieldAndDirectives);

        for(TableSeed tableSeed : tableSeeds){
            Set<String> commonFields = commonFieldAndValues.keySet();
            for(String commonField : commonFields){
                tableSeed.updateDirective(commonField, commonFieldAndValues.get(commonField));
            }
        }
        return tableSeeds;
    }

    private Map<String, String> getCommonFieldAndDirective(TableFactory tableFactory, List<TableSeed> tableSeeds){
        Map<String, String> firstFieldAndDirective = tableSeeds.get(0).getFieldAndDirective(tableFactory);
        Set<String> commonFields = firstFieldAndDirective.keySet();
        int tableSeedBundleSize = tableSeeds.size();
        for(int i = 1; i < tableSeedBundleSize; i++){
            commonFields.retainAll(tableSeeds.get(i).getFieldAndDirective(tableFactory).keySet());
        }

        Map<String, String> commonFieldAndDirectives = new HashMap<String, String>(commonFields.size());
        for(String commonField : commonFields){
            commonFieldAndDirectives.put(commonField, firstFieldAndDirective.get(commonField));
        }
        return commonFieldAndDirectives;
    }

    public int getRecordNum() {
        return recordNum;
    }
}
