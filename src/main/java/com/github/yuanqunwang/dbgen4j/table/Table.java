package com.github.yuanqunwang.dbgen4j.table;

import java.util.*;

public class Table {
    private String tableName;
    private List<String> fields;
    private List<? extends List<String>> records;
    private Map<String, Integer> fieldsIndex;

    public Table(String tableName, List<String> fields) {
        this(tableName, fields, new LinkedList<List<String>>());
    }

    public Table(String tableName, List<String> fields, List<? extends List<String>> records){
        this.tableName = tableName;
        this.fields = fields;
        this.records =records;
        initFieldIndex();
    }

    private void initFieldIndex(){
        fieldsIndex = new HashMap<String, Integer>(fields.size());
        int index = 0;
        for(String field : fields){
            fieldsIndex.put(field, index++);
        }
    }

//    public void insert(ArrayList<String> record) {
//        records.add(record);
//    }

    public String toString() {
        StringBuilder sbSql = new StringBuilder();
        sbSql.append("INSERT INTO `");
        sbSql.append(this.tableName);
        sbSql.append("`(");
        sbSql.append(getStringFields());
        sbSql.append(")");
        sbSql.append(" VALUES");
        sbSql.append(getStringRecords());
        sbSql.append(";");

        return sbSql.toString();
    }

    public void update(int rowIndex, Map<String, String> map){
        int totalRows = records.size();
        if(rowIndex < 0 || rowIndex >= totalRows){
            throw new RuntimeException("rowIndex invalid, update table failed,");
        }
        List<String> needUpdate = records.get(rowIndex);
        for(String field : map.keySet()){
            Integer index = fieldsIndex.get(field);
            if(index == null){
                throw new RuntimeException("not support field: \"" + field + "\" in table " + this.tableName);
            }
            String value = map.get(field);
            if(value != null){
                needUpdate.set(index, value);
            }
        }
    }

    public void updateAll(List<Map<String, String>> records){
        int recordNum = records.size();
        for(int i = 0; i < recordNum; i++){
            update(i, records.get(i));
        }
    }



    public String getTableName() {
        return tableName;
    }

    public List<String> getFields() {
        return fields;
    }

    public List<? extends List<String>> getRecords() {
        return records;
    }

    /**
     * table fields
     * @return
     */
    private String getStringFields() {
        return mergeFields(fields, "`");
    }

    public Map<String, Integer> getFieldsIndex() {
        return fieldsIndex;
    }

    /**
     * table values
     * @return
     */
    private String getStringRecords() {
        List<String> rows = new ArrayList<String>(fields.size());
        for(List<String> record : records) {
            rows.add(mergeFields(record, "\'"));
        }

//		return mergeFields(rows, "\n");
        StringBuilder sb = new StringBuilder();
        String delimiter = "\n";
        for(String row : rows) {
            sb.append(delimiter);
            delimiter = ",\n";
            sb.append("(");
            sb.append(row);
            sb.append(")");
        }

        return sb.toString();
    }


    /**
     * convert Collection data to String
     * each entry surrounding with quotation
     * delimit by comma , mark
     * @param values
     * @param quotation
     * @return
     */
    private String mergeFields(List<String> values, String quotation) {
        StringBuilder sb = new StringBuilder();
        String prefix = "" + quotation;
        for(String field : values) {
            sb.append(prefix);
            prefix = "," + quotation;
            sb.append(field);
            sb.append(quotation);
        }
        return sb.toString();
    }
}
