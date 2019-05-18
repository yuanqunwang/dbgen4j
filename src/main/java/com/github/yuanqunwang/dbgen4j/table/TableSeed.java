package com.github.yuanqunwang.dbgen4j.table;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableSeed{
    private final Pattern EXPRESSION_PATTERN = Pattern.compile("#\\{([a-z0-9A-Z_.]+)\\s?(?:'([^']+)')?(?:,'([^']+)')*\\}");
    private String tableName;
    private Map<String, String> fieldAndDirectiveSeed;

    public TableSeed(String tableName, Map<String, String> fieldAndDirective){
        this.tableName =  tableName;
        this.fieldAndDirectiveSeed = fieldAndDirective;
    }

    public Map<String, String> getFieldAndDirective(TableFactory tableFactory){
        return commonDirective(tableFactory);
    }

    public String getTableName() {
        return tableName;
    }

    private Map<String, String> commonDirective(TableFactory tableFactory){
        Map<String, String> fieldAndDirective = new HashMap<String, String>(fieldAndDirectiveSeed);
        Map<String, List<String>> directiveAndField = new HashMap<String, List<String>>();

        for(String field : fieldAndDirective.keySet()){
            String directives = fieldAndDirective.get(field);
            Matcher matcher = EXPRESSION_PATTERN.matcher(directives);
            while(matcher.find()){
                String directive = matcher.group(0);
                List<String> fields = directiveAndField.get(directive);
                if(fields == null){
                    fields = new ArrayList<String>(3);
                }
                fields.add(field);
                directiveAndField.put(directive, fields);
            }
        }

        Map<String, String> commonDirective = new HashMap<String, String>();
        for(String directive : directiveAndField.keySet()){
            List<String> fields = directiveAndField.get(directive);
            if(fields.size() > 1){
                commonDirective.put(directive, directive);
            }
        }


        Table table = tableFactory.createTable("commonField", commonDirective, 1);
        for(String directive : directiveAndField.keySet()){
            List<String> fields = directiveAndField.get(directive);
            if(fields.size() > 1){
                for(String field : fields){
                    Integer index = table.getFieldsIndex().get(directive);
                    String value = table.getRecords().get(0).get(index);
                    String fieldDirective = fieldAndDirective.get(field);
                    value = StringUtils.replaceOnce(fieldDirective, directive, value);
                    fieldAndDirective.put(field, value);
                }
            }
        }
        return fieldAndDirective;
    }
}

