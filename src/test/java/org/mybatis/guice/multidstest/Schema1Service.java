package org.mybatis.guice.multidstest;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Transactional;

public class Schema1Service {
    @Inject
    private Schema1Mapper mapper;
    
    @Transactional
    public void createSchema1() {
        mapper.createSchema1Step1();
        mapper.createSchema1Step2();
        mapper.createSchema1Step3();
    }
    
    @Transactional
    public Integer getNextValueFromSchema1() {
        return mapper.getNextValueFromSchema1();
    }
}
