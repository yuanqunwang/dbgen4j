package com.github.yuanqunwang.dbgen4j.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapUtil {
    /**
     * a key's value determined by other key's value
     * key reference have the form of '@{referencedKey1}'
     * after resolving, this function will return value of the reference key combined.
     * @param keyValue
     * @param reference
     * @return
     */
    public static String resolveFieldReference(Map<String, String> keyValue, String reference){
        Pattern referencePattern = Pattern.compile("@\\{([a-zA-Z0-9]+)\\}");
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
     * @param keyValue
     */
    public static void resolveReference(Map<String, String> keyValue){
        Pattern referencePattern = Pattern.compile("@\\{([a-zA-Z0-9]+)\\}");
        Set<String> keySet = keyValue.keySet();
        for(String key : keySet){
            String value = keyValue.get(key);
            Matcher matcher = referencePattern.matcher(value);
            if(matcher.find()){
                String referencedValue = resolveFieldReference(keyValue, value);
                keyValue.put(key, referencedValue);
            }
        }
    }

}
