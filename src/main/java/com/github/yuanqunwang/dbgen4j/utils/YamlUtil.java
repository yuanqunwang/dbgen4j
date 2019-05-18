package com.github.yuanqunwang.dbgen4j.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlUtil {


    public static Map loadValues(String filename) {

        InputStream stream = null;
        stream = findStream(filename);

        if (stream == null) {
            return null;
        }

        final Map valuesMap = new Yaml().loadAs(stream, Map.class);
        return valuesMap;
    }

    private static InputStream findStream(String filename) {
        InputStream streamOnClass = YamlUtil.class.getResourceAsStream(filename);
        if (streamOnClass != null) {
            return streamOnClass;
        }
        return YamlUtil.class.getClassLoader().getResourceAsStream(filename);
    }
}
