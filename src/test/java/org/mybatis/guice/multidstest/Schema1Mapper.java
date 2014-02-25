package org.mybatis.guice.multidstest;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface Schema1Mapper {
    
    @Update({
        "drop schema schema1 if exists cascade;"
    })
    int createSchema1Step1();

    @Update({
        "create schema schema1;"
    })
    int createSchema1Step2();

    @Update({
        "CREATE SEQUENCE schema1.test_sequence AS INTEGER START WITH 100 INCREMENT BY 1;"
    })
    int createSchema1Step3();

    @Select({
        "call next value for schema1.test_sequence"
    })
    Integer getNextValueFromSchema1();
}
