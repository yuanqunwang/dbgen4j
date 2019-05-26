package com.github.yuanqunwang.dbgen4j.table;

import com.github.yuanqunwang.dbgen4j.utils.MapUtil;

import java.util.*;

/**
 * a class mapping to a table in relation database.
 * fieldAndIndex representing the table fields and its corresponding position as a key-value map
 */




public class Table {
    private String tableName;
    private Map<String, Integer> fieldsAndIndex;
    private List<ArrayList<String>> records;

    public Table(String tableName, List<String> fields) {
        this(tableName, fields, new LinkedList<ArrayList<String>>());
    }

    public Table(String tableName, List<String> fields, List<ArrayList<String>> records){
        check(fields.size(), records);
        this.tableName = tableName;
        initFieldAndIndex(fields);
        this.records =records;
    }



    /**
     * insert a record to table.
     * @param record
     */
    public void insert(ArrayList<String> record) {
        int providedFieldNum = record.size();
        int tableFieldNum = fieldsAndIndex.size();
        if(providedFieldNum != tableFieldNum){
            throw new RuntimeException("record number should be of length: " + tableFieldNum
                    + ", but " + providedFieldNum + " provided.");
        }
        records.add(record);
    }

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

    /**
     * @param rowIndex indicate the row to be updated
     * @param map key representing the field, value representing the updated value
     *            if value is null, this function will not update the specific field.
     */
    public void updateRecord(int rowIndex, Map<String, String> map){
        int totalRows = records.size();
        if(rowIndex < 0 || rowIndex >= totalRows){
            throw new IndexOutOfBoundsException("rowIndex overflow, update table failed,");
        }
        List<String> rowNeededUpdate = records.get(rowIndex);
        Set<String> fieldNeededUpdate = map.keySet();

        for(String field : fieldNeededUpdate){
            Integer index = fieldsAndIndex.get(field);
            if(index == null){
                throw new RuntimeException("field: \"" + field + "\" not supported in table " + this.tableName);
            }
            String value = map.get(field);
            if(value != null){
                rowNeededUpdate.set(index, value);
            }
        }
    }

    public void updateAllRecords(List<Map<String, String>> records){
        int recordNum = this.records.size();
        int providedNum = records.size();
        if(recordNum != providedNum){
            throw new RuntimeException("updateAllRecord failed, provided record number not right.");
        }
        for(int i = 0; i < recordNum; i++){
            updateRecord(i, records.get(i));
        }
    }



    public String getTableName() {
        return tableName;
    }

    public List<String> getFields() {
        return new ArrayList<String>(this.fieldsAndIndex.keySet());
    }

    public List<? extends List<String>> getRecords() {
        return records;
    }

    public List<String> getSingleRecord(int recordIndex){
        if(recordIndex < 0 || recordIndex >= records.size()){
            throw new IndexOutOfBoundsException("recordIndex:" + recordIndex);
        }
        return records.get(recordIndex);
    }


    public String getSpecificField(int recordIndex, String field){
        List<String> record = getSingleRecord(recordIndex);
        Integer recordPosition = fieldsAndIndex.get(field);
        return record.get(recordPosition);
    }

    /**
     * if a field's value is reference other field in the same table,
     * this function can resolve the value.
     * reference in the form of "@{referenced_field}"
     * @param recordIndex
     */
    public void resolveReference(int recordIndex){
        List<String> field = this.getFields();
        List<String> record = this.getSingleRecord(recordIndex);
        Map<String, String> fieldAndRecord = MapUtil.mergeMap(field, record);
        fieldAndRecord = MapUtil.resolveReference(fieldAndRecord);
        this.updateRecord(recordIndex,fieldAndRecord);
    }

    /**
     * table fields
     * @return
     */
    private String getStringFields() {
        List<String> fields = new ArrayList<String>(fieldsAndIndex.keySet());
        return mergeFields(fields, "`");
    }

    public Map<String, Integer> getFieldsIndex() {
        return fieldsAndIndex;
    }

    /**
     * table values
     * @return
     */
    private String getStringRecords() {
        List<String> rows = new ArrayList<String>(fieldsAndIndex.size());
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



    private void initFieldAndIndex(List<String> fields){
        fieldsAndIndex = new LinkedHashMap<String, Integer>(fields.size());
        int index = 0;
        for(String field : fields){
            fieldsAndIndex.put(field, index++);
        }
    }

    /**
     * check field number and record number for equality
     * @param fieldNum
     * @param records
     */
    private void check(int fieldNum, List<ArrayList<String>> records){
        Set<Integer> providedFieldNum = new HashSet<Integer>();
        for(List<String> record : records){
            providedFieldNum.add(record.size());
        }
        if(providedFieldNum.size() != 1 || !providedFieldNum.contains(fieldNum) || records.size() == 0){
            throw new RuntimeException("Field or record number not right.");
        }

    }
}
