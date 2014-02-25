package org.mybatis.guice.multidstest;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface Schema2Mapper {
    
    @Update({
        "drop schema schema2 if exists cascade;"
    })
    int createSchema2Step1();

    @Update({
        "create schema schema2;"
    })
    int createSchema2Step2();

    @Update({
        "CREATE SEQUENCE schema2.test_sequence AS INTEGER START WITH 200 INCREMENT BY 1;"
    })
    int createSchema2Step3();

    @Select({
        "call next value for schema2.test_sequence"
    })
    Integer getNextValueFromSchema2();
}
