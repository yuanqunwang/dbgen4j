package com.github.yuanqunwang.dbgen4j.table;

import java.util.*;

/**
 * multi {@Link Table} having relation to each other by primary key.
 */
public class TableBundle extends ArrayList<Table>{
    private int recordLen;
    private Set<String> commonKeys;

    TableBundle(List<Table> tables, int recordLen){
        super(tables);
        this.recordLen = recordLen;
//        initCommonKeys();
//        correlateTables();
    }

    void initCommonKeys(){
        int tableNum = size();
        commonKeys = new HashSet<String>(get(0).getFields());
        for(int i = 1; i < tableNum; i++){
            commonKeys.retainAll(get(i).getFields());
        }
    }

    void correlateTables(){
        List<Map<String, String>> commonKeysAndValues = new ArrayList<Map<String, String>>(size());
        Table firstTable = get(0);
        List<? extends List<String>> firstTableRecords = firstTable.getRecords();
        Map<String, Integer> fieldIndex = firstTable.getFieldsIndex();
        for(List<String> record : firstTableRecords){
            commonKeysAndValues.add(keyValue(record, fieldIndex));
        }

        for(Table table : this){
            table.updateAll(commonKeysAndValues);
        }
    }

    Map<String, String> keyValue(List<String> record, Map<String, Integer> fieldIndex){
        Map<String, String> keyValue = new HashMap<String, String>(commonKeys.size());
        for(String key : commonKeys){
            Integer index = fieldIndex.get(key);
            String value = record.get(index);
            keyValue.put(key, value);
        }
        return keyValue;
    }

}
