package com.github.yuanqunwang.dbgen4j.table;

import com.github.yuanqunwang.dbgen4j.utils.FakerUtil;
import java.util.*;

/**
 */
public class TableBundleFactory {
    private final TableSeedBundle tableSeedBundle;

    public TableBundleFactory(Map<String, Map<String, String>> tableNameAndFieldDirectives){
        Set<String> tableNames = tableNameAndFieldDirectives.keySet();
        List<TableFactory.TableSeed> tableSeeds = new ArrayList<TableFactory.TableSeed>(tableNameAndFieldDirectives.size());
        for(String tableName: tableNames){
            TableFactory.TableSeed tableSeed = new TableFactory.TableSeed(tableName, tableNameAndFieldDirectives.get(tableName));
            tableSeeds.add(tableSeed);
        }
        this.tableSeedBundle = new TableSeedBundle(tableSeeds);
    }


    public TableBundle createTableBundle(int recordLen){
        List<Table> tableList = new ArrayList<Table>(this.tableSeedBundle.size());
        for(TableFactory.TableSeed tableSeed : this.tableSeedBundle){
            TableFactory tableFactory = new TableFactory(tableSeed.getTableName(), tableSeed);
            Table table = tableFactory.createNotResolvedTable(recordLen);
            tableList.add(table);
        }
        makeCommonFieldSameAndResolveReference(tableList, recordLen);
        return new TableBundle(tableList, recordLen);
    }

    private void makeCommonFieldSameAndResolveReference(List<Table> tableList, int recordLen){
        Map<String, String> commonFieldAndDirective = this.tableSeedBundle.getCommonFieldAndDirective();
        for(int i = 0; i < recordLen; i++){
            Map<String, String> commonFieldAndValue = FakerUtil.genFakeKeyValueMap(commonFieldAndDirective);
            for(Table table : tableList){
                table.updateRecord(i, commonFieldAndValue);
                table.resolveReference(i);
            }
        }
    }

    /**
     * multi {@Link TableSeed} having relation to each other
     */
    class TableSeedBundle extends ArrayList<TableFactory.TableSeed> {


        /**
         * create a {@Link TableSeedBundle} with a List of TableSeed and recordNum
         * @param tableSeedList representing a List of {@Link TableSeed}
         */
        TableSeedBundle(List<TableFactory.TableSeed> tableSeedList){
            super(Collections.unmodifiableCollection(tableSeedList));
        }

        Map<String, String> getCommonFieldAndDirective(){
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
            Map<String, String> commonFieldAndDirective = new HashMap<String, String>(commonFields.size());
            for(String commonField : commonFields){
                commonFieldAndDirective.put(commonField, firstFieldAndDirective.get(commonField));
            }
            return commonFieldAndDirective;
        }

    }
}
