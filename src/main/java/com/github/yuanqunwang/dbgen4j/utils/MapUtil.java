package com.github.yuanqunwang.dbgen4j.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapUtil {
    /*
     * field reference regex pattern.
     */
    private static Pattern referencePattern = Pattern.compile("@\\{([a-zA-Z0-9_]+)\\}");


    /**
     * a key's value determined by other key's value
     * key reference have the form of '@{referencedKey1}'
     * after resolving, this function will return value of the reference key combined.
     */
    public static String resolveFieldReference(Map<String, String> keyValue, String reference){
        Matcher matcher = referencePattern.matcher(reference);
        while(matcher.find()){
            String referencedKey = matcher.group(1);
            String referencedValue = keyValue.get(referencedKey);
            if(referencedValue == null){
                throw new RuntimeException("referenced key: " + referencedKey + " does not exist");
            }
            String escapedReference = matcher.group(0);
            reference = StringUtils.replaceOnce(reference, escapedReference, referencedValue);

            /*
             * if the resolved value also having the reference form, we need resolve it too.
             */
            reference = resolveFieldReference(keyValue, reference);
        }
        return reference;
    }

    /**
     * resolve all the reference and update corresponding value
     */
    public static Map<String, String> resolveReference(Map<String, String> keyValue){
        Set<String> keySet = keyValue.keySet();
        for(String key : keySet){
            String value = keyValue.get(key);
            Matcher matcher = referencePattern.matcher(value);
            if(matcher.find()){
                String referencedValue = resolveFieldReference(keyValue, value);
                keyValue.put(key, referencedValue);
            }
        }
        return keyValue;
    }

    /**
     * merge two lists to a HashMap
     */
    public static <K, V> Map<K, V> mergeMap(List<K> keys, List<V> values){
        int keySize = keys.size();
        int valueSize = values.size();
        assert(keySize == valueSize);
        Map<K, V> kvMap = new LinkedHashMap<K, V>(keySize);
        for(int i = 0; i < keySize; i++){
            K key = keys.get(i);
            V value = values.get(i);
            kvMap.put(key, value);
        }
        return kvMap;
    }

    public static <K, V> Map<K, V> getRetainMap(Map<K, V> map, Collection<K> keys){
        Set<K> mapKey = map.keySet();
        mapKey.retainAll(keys);

        Map<K, V> resultMap = new HashMap<K, V>();
        for(K k : mapKey){
            V mapValue = map.get(k);
            resultMap.put(k, mapValue);
        }
        return resultMap;
    }
}
