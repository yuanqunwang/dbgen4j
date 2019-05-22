package com.github.yuanqunwang.dbgen4j.sfck;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeSeed;
import com.github.yuanqunwang.dbgen4j.seed.Sfck;
import com.github.yuanqunwang.dbgen4j.table.Table;
import com.github.yuanqunwang.dbgen4j.table.TableFactory;
import com.github.yuanqunwang.dbgen4j.table.TableSeed;
import com.github.yuanqunwang.dbgen4j.table.TableSeedBundle;
import com.github.yuanqunwang.dbgen4j.utils.YamlUtil;

import java.util.*;

public class Wlck {
    private Faker faker;
    private TableFactory tableFactory;
    private List<TableSeed> tableSeeds;
    private List<Table> tables;
    private Map<String, Object> message;
    private List<TableSeedBundle> tableSeedsBundles = new LinkedList<TableSeedBundle>();
    private List<TableBundle> tableBundles = new LinkedList<TableBundle>();

    public Wlck(){
        initFakerAndTableFactory();
        initMessage();
        tableFactory = new TableFactory(faker, tableSeedsBundles.get(0));
        generateTable();
//        meaningfulTable();
    }

    private void initMessage(){
        message = YamlUtil.loadValues("message.yml");
        Map<String, Object> wlck = YamlUtil.loadValues("wlck.yml");

        Map<String, Object> specificTableSeeds = (Map<String, Object>)wlck.get("specific");
        for(String key : message.keySet()){
            tableSeeds = new ArrayList<TableSeed>(3);

            List<Object> list = (List<Object>) message.get(key);
            if(list != null){
                String tableName = key;
                int recordNum = list.size();

                tableSeeds.addAll(getTableSeeds(wlck, "public"));
                tableSeeds.add(new TableSeed(tableName, (Map<String, String>) specificTableSeeds.get(tableName)));

                this.tableSeedsBundles.add(new TableSeedBundle(recordNum));

            }
        }
    }

    private List<TableSeed> getTableSeeds(Map map, String domain){
        Map<String, Object> publicSeed = (Map<String, Object>)map.get(domain);
        List<TableSeed> tableSeeds = new ArrayList<TableSeed>(publicSeed.size());
        for(String pubTable : publicSeed.keySet()){
            TableSeed tableSeed = new TableSeed(pubTable, (Map<String, String>) publicSeed.get(pubTable));
            tableSeeds.add(tableSeed);
        }
        return tableSeeds;
    }



    private void initFakerAndTableFactory(){
        faker = new Faker(new Locale("zh-CN"));
        FakeSeed fakeSeed = new FakeSeed("seed/sfck.yml", Sfck.class);
        faker.addFakeSeed(fakeSeed);
        tableFactory = new TableFactory(faker);
    }

    class TableBundle{
        List<Table> tables;
        Set<String> commonKeys;

        TableBundle(List<Table> tables){
            this.tables = tables;
            initCommonKeys();
            correlateTables();
        }

        void initCommonKeys(){
            int tableNum = tables.size();
            commonKeys = new HashSet<String>(tables.get(0).getFields());
            for(int i = 1; i < tableNum; i++){
                commonKeys.retainAll(tables.get(i).getFields());
            }
        }

        void correlateTables(){
            List<Map<String, String>> commonKeysAndValues = new ArrayList<Map<String, String>>(tables.size());
            Table firstTable = tables.get(0);
            List<? extends List<String>> firstTableRecords = firstTable.getRecords();
            Map<String, Integer> fieldIndex = firstTable.getFieldsIndex();
            for(List<String> record : firstTableRecords){
                commonKeysAndValues.add(keyValue(record, fieldIndex));
            }

            for(Table table : tables){
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

    private void generateTable(){
        List<Table> tableList = tableFactory.createTableBundle();
        for(Table table : tableList){
            System.out.println(table.toString());
        }
    }


    private List<Table> createTable(List<TableSeed> tableSeedList){
        for(TableSeed tableSeed : tableSeedList){
//            Table table = tableFactory.createTable(tableSeed.getTableName(), tableSeed.getFieldAndDirective(tableFactory));
        }
        return null;
    }




    private void meaningfulTable(){
        int i = 0;
        for (String key : message.keySet()) {
            List<Map<String, String>> list = (List<Map<String, String>>) message.get(key);
            if (list != null) {
                TableBundle tableBundle = tableBundles.get(i++);
                for(Table table : tableBundle.tables){
                    table.updateAll(list);
                }
            }
        }
    }


    public List<TableBundle> getTableBundles(){
        return tableBundles;
    }

    public List<Table> getTables () {
        return tables;
    }

    public static void main(String[] args){
        Wlck wlck = new Wlck();
        List<TableBundle> tableBundles = wlck.getTableBundles();
//        for(TableBundle tableBundle : tableBundles){
//            for(Table table : tableBundle.tables){
//                System.out.println(table.toString());
//                System.out.println();
//            }
//            System.out.println();
//            System.out.println();
//            System.out.println();
//        }
    }

}
