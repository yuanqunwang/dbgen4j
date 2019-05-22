package com.github.yuanqunwang.dbgen4j.table;

import java.util.*;

/**
 * multi {@Link TableSeed} having relation to each other
 */
public class TableSeedBundle extends ArrayList<TableSeed> {
    private int recordNum;
    private Map<String, String> commonFiedlAndDirective;

    /**
     * create a empty {@Link TableSeedBundle}
     * by calling {@Link List}'s add method to add {@Link TableSeed}
     * @param recordNum
     */
    public TableSeedBundle(int recordNum){
        super();
        this.recordNum = recordNum;
    }


    /**
     * create a {@Link TableSeedBundle} with a List of TableSeed and recordNum
     * @param tableSeedList representing a List of {@Link TableSeed}
     * @param recordNum record number each table has
     */
    public TableSeedBundle(List<TableSeed> tableSeedList, int recordNum){
        super(Collections.unmodifiableCollection(tableSeedList));
        this.recordNum = recordNum;
    }


    /**
     * by making same field having same value
     * to make {@Link TableSeedBundle} having relation to each other,
     * @param tableFactory
     * @return
     */
    public List<TableSeed> nextTableSeedBundle(TableFactory tableFactory) {
        return populateCommonField(tableFactory);
    }

    private List<TableSeed> populateCommonField(TableFactory tableFactory){
        List<TableSeed> tableSeeds = new ArrayList<TableSeed>();
        for(TableSeed tableSeed : this){
            tableSeeds.add(tableSeed);
        }

//        Map<String, String> commonFieldAndDirectives = getCommonFieldAndDirective();
////        Map<String, String> commonFieldAndValues = tableFactory.createSingleRecord(commonFieldAndDirectives);
////
////        for(TableSeed tableSeed : tableSeeds){
////            Set<String> commonFields = commonFieldAndValues.keySet();
////            for(String commonField : commonFields){
////                tableSeed.updateDirective(commonField, commonFieldAndValues.get(commonField));
////            }
////        }
        return tableSeeds;
    }

    public Map<String, String> getCommonFieldAndDirective(){
        /*
         * find common fields
         */
        Map<String, String> firstFieldAndDirective = get(0);
        Set<String> commonFields = firstFieldAndDirective.keySet();
        int tableSeedBundleSize = size();
        for(int i = 1; i < tableSeedBundleSize; i++){
            commonFields.retainAll(get(i).keySet());
        }

        /*
         * fill common fields with corresponding directive
         */
        Map<String, String> commonFieldAndDirectives = new HashMap<String, String>(commonFields.size());
        for(String commonField : commonFields){
            commonFieldAndDirectives.put(commonField, firstFieldAndDirective.get(commonField));
        }
        return commonFieldAndDirectives;
    }
//
//    private void getCommonDirectivesInTableSeedBundle(){
//        for(TableSeed tableSeed : this){
//            Map<String, List<String>>
//        }
//    }

    public int getRecordNum() {
        return recordNum;
    }
}
