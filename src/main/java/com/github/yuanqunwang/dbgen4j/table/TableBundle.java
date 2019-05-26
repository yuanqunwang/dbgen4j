package com.github.yuanqunwang.dbgen4j.table;

import com.github.yuanqunwang.dbgen4j.utils.MapUtil;

import java.util.*;

/**
 * multi {@Link Table} having relation to each other by primary key.
 */
public class TableBundle extends ArrayList<Table>{
    private int recordLen;

    TableBundle(List<Table> tables, int recordLen){
        super(tables);
        this.recordLen = recordLen;
    }

    public void populateFieldWithValue(List<Map<String, String>> customFieldAndValue){
        for(int i = 0; i < recordLen; i++){
            Map<String, String> fieldValue = customFieldAndValue.get(i);
            for(Table table : this){
                List<String> keys = table.getFields();
                Map<String, String> retainMap = MapUtil.getRetainMap(fieldValue, keys);
                table.updateRecord(i, retainMap);
            }
        }
    }
}
