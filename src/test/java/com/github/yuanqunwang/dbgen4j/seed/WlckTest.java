package com.github.yuanqunwang.dbgen4j.seed;

import com.github.yuanqunwang.dbgen4j.sfck.Wlck;
import com.github.yuanqunwang.dbgen4j.table.Table;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.List;

public class WlckTest {

    @Test
    public void test(){
        Wlck wlck = new Wlck();
        List<Table> tables = wlck.getTables();
        for(Table table : tables){
            System.out.println(table.toString());
        }

    }

    @Test
    public void testReplace(){
        String result = StringUtils.replaceOnce("#{phoneNumber.cellPhone}", "#{phoneNumber.cellPhone}", "14569869493");
        System.out.println(result);
    }
}
