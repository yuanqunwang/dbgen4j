package com.github.yuanqunwang.dbgen4j.seed;

import com.github.javafaker.Faker;

public class Sfck {
    private final Faker faker;

    public Sfck(Faker faker){
        this.faker = faker;
    }

    public String id_type(){
        return this.faker.resolve("sfck.id_type");
    }

    public String ccy(){
        return this.faker.resolve("sfck.ccy");
    }

    public String cxlx(){
        return this.faker.resolve("sfck.cxlx");
    }

    public String bank_code(){
        return this.faker.resolve("sfck.bank_code");
    }

    public String org_code(){
        return this.faker.resolve("sfck.org_code");
    }
}
