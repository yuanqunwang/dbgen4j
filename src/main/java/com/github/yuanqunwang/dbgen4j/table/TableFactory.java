package com.github.yuanqunwang.dbgen4j.table;


import com.github.yuanqunwang.dbgen4j.utils.FakerUtil;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>
 * This class is the factory of the {@Link Table}.
 * A Table Factory can be constructed by given:
 * <ul>
 *     <li>Field: representing the table field.</li>
 *     <li>Directive: sources of the generated fake values.</li>
 * </ul>
 * so far, directives have the following form
 * <ul>
 *     <li>"#{Address.city}", representing the java-faker's method {@Link Address#city()}</li>
 *     <li>
 *         "@{reference_field}", representing referenced field value in the same table,
 *         it's useful when a field's value depends on other fields's value in the same table.
 *     </li>
 *     <li>any characters</li>
 *     <li>above three forms combined</li>
 * </ul>
 * </p>
 */





public class TableFactory {
    private final TableSeed tableSeed;

    /**
     * @param tableName table name
     * @param fieldAndDirectives fields and directives key-value pairs, sources of the generated fake value table
     */
    public TableFactory(String tableName, Map<String, String> fieldAndDirectives){
        this.tableSeed = new TableSeed(tableName, fieldAndDirectives);
    }

    /**
     * generate fake value table, but field references not revolved.
     * @param recordLen length of the generated fake values;
     * @return  a {@Link Table} whose field are not resolved
     */
    Table createNotResolvedTable(int recordLen){
        List<ArrayList<String>> records = fakeMultiRecords(new ArrayList<String>(tableSeed.values()), recordLen);
        return new Table(tableSeed.getTableName(), new ArrayList<String>(tableSeed.keySet()), records);
    }

    /**
     * generate fake value table, field references also revolved.
     * @param recordLen length of the generated fake values;
     * @return a {@Link Table} whose field are properly resolved
     */
    public Table createTable(int recordLen){
        Table table = createNotResolvedTable(recordLen);
        for(int i = 0; i < recordLen; i++){
            table.resolveReference(i);
        }
        return table;
    }

    /**
     * use {@Link FakeUtil#genFakeValueList()} to generate list of values
     */
    private List<ArrayList<String>> fakeMultiRecords(List<String> directives, int recordLen){
        List<ArrayList<String>> records = new ArrayList<ArrayList<String>>(recordLen);
        for(int i = 0; i < recordLen; i++){
            records.add(FakerUtil.genFakeValueList(directives));
        }
        return records;
    }

    /**
     * encapsulation of fieldAndDirective {@Link Map}
     */
   static class TableSeed extends LinkedHashMap<String, String>{
        /*
         * regular expression representing directive that can generate fake value by calling Faker's expression(directive) method
         */
        private final Pattern EXPRESSION_PATTERN = Pattern.compile("#\\{([a-z0-9A-Z_.]+)\\s?(?:'([^']+)')?(?:,'([^']+)')*\\}");
        private String tableName;

        /**
         * construct a {@Link TableSeed} with tableName and fieldAndDirective
         */
        TableSeed(String tableName, Map<String, String> fieldAndDirective){
            super(Collections.unmodifiableMap(fieldAndDirective));
            this.tableName =  tableName;
        }

        String getTableName() {
            return tableName;
        }


        /**
         * field index in map
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
}
