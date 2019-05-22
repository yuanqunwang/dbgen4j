package com.github.yuanqunwang.dbgen4j.table;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * key-value pairs representing table fields and directives from which table values are generated.
 * directive has a form of "#{Name.name}", first Name representing the @{@Link Name} class, while
 * the last name representing the name() method of the preceding #{@Link Name} class.
 * all supported directive can reference javafaker's complete fake Class and its method.
 */
public class TableSeed extends HashMap<String, String>{
    /*
     * regular expression representing directive that can generate fake value by calling Faker's expression(directive) method
     */
    private final Pattern EXPRESSION_PATTERN = Pattern.compile("#\\{([a-z0-9A-Z_.]+)\\s?(?:'([^']+)')?(?:,'([^']+)')*\\}");
    private String tableName;

    /**
     * construct a empty {@Link TableSeed} with only tableName initialized
     * by calling Map's put(key,value) method to add key-value pair later
     * @param tableName
     */
    public TableSeed(String tableName){
        this(tableName, new HashMap<String, String>());
    }

    /**
     * construct a {@Link TableSeed} with tableName and fieldAndDirective
     * @param tableName
     * @param fieldAndDirective field representing table, directive representing {@Link Faker}'s
     */
    public TableSeed(String tableName, Map<String, String> fieldAndDirective){
        super(fieldAndDirective);
        this.tableName =  tableName;
    }

    public void updateDirective(String field, String value){
        this.put(field, value);
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * fields having same directive will generate same value
     * for example,
     * field1 = '#{Name.name}' and
     * field2 = '#{Name.name}-#{PhoneNumber.cellphone}',
     *
     * by calling this method will generate list of directive like:
     * field1='John'
     * field2='John-#{PhoneNumber.cellphone}'
     * @param tableFactory
     * @return
     */
    public List<String> nextDirective(TableFactory tableFactory){

        //all field have same directive, directive as key, and fields as a list value
        Map<String, List<String>> commonDirectiveAndField = getAllDirectiveFieldMap();

        //find all directive that exist in more than one field
        Map<String, String> commonDirective = getDirectiveExistInMoreThanOneField(commonDirectiveAndField);

        return getResultDirective(commonDirectiveAndField, commonDirective, tableFactory);
    }

    /**
     * call getResultDirective, and put keys and values in map
     * @param tableFactory
     * @return
     */
    public Map<String, String > nextFieldAndDirective(TableFactory tableFactory){
        Map<String, String> resultMap = new HashMap<String, String>(size());
        Set<String> keySet = keySet();
        List<String> nextDirective = nextDirective(tableFactory);
        int index = 0;
        for(String key : keySet){
            resultMap.put(key,nextDirective.get(index++));
        }
        return resultMap;
    }

    /**
     * replace the directives exit in more than one field with same generated value,
     * those directives exit only in one field remain unchanged.
     * @param commonDirectiveAndField
     * @param commonDirective
     * @param tableFactory
     * @return
     */
    private List<String> getResultDirective(Map<String, List<String>> commonDirectiveAndField, Map<String, String > commonDirective, TableFactory tableFactory){
        ArrayList<String> resultDirectives = new ArrayList<String>(this.values());
        Map<String, String> commonDirectiveValue = tableFactory.createSingleRecord(commonDirective);
        Map<String, Integer> fieldIndexMap = initFieldIndex();

        Set<String> commonDirectiveSet = commonDirective.keySet();
        for(String common : commonDirectiveSet){
            List<String> commonFields = commonDirectiveAndField.get(common);
            String commonValue = commonDirectiveValue.get(common);
            for(String field : commonFields){
                int fieldIndex = fieldIndexMap.get(field);
                String directive = resultDirectives.get(fieldIndex);
                directive = StringUtils.replaceOnce(directive, common, commonValue);
                resultDirectives.set(fieldIndex, directive);
            }
        }

        return resultDirectives;
    }

    /**
     * find the directives that exist in more than one field,
     * and remove the directives keys exist only in one field.
     * @param commonDirectiveAndField
     * @return
     */
    private Map<String, String> getDirectiveExistInMoreThanOneField(Map<String, List<String>> commonDirectiveAndField){
        Map<String, String> commonDirective = new HashMap<String, String>();
        for(String directive : commonDirectiveAndField.keySet()){
            List<String> fields = commonDirectiveAndField.get(directive);
            if(fields.size() > 1){
                commonDirective.put(directive, directive);
            }else {
                commonDirective.remove(directive);
            }
        }
        return commonDirective;
    }

    /**
     * find every directive exists in every field, and put the directive in a map as key,
     * all fields that having the key as value.
     * @return
     */
    public Map<String, List<String>> getAllDirectiveFieldMap(){
        Map<String, List<String>> commonDirectiveAndField = new HashMap<String, List<String>>();

        for(String field : this.keySet()){
            String directives = this.get(field);
            Matcher matcher = EXPRESSION_PATTERN.matcher(directives);
            while(matcher.find()){
                String directive = matcher.group(0);
                List<String> fields = commonDirectiveAndField.get(directive);
                if(fields == null){
                    fields = new ArrayList<String>(3);
                }
                fields.add(field);
                commonDirectiveAndField.put(directive, fields);
            }
        }

        return commonDirectiveAndField;
    }

    /**
     * field index in map
     * @return
     */
    private Map<String, Integer> initFieldIndex(){
        Map<String, Integer> fieldIndex = new HashMap<String, Integer>(this.keySet().size());
        int index = 0;
        for(String field : this.keySet()){
            fieldIndex.put(field, index++);
        }
        return fieldIndex;
    }
}

