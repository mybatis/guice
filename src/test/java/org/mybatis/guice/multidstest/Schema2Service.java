package org.mybatis.guice.multidstest;

import javax.inject.Inject;

import org.mybatis.guice.transactional.Transactional;

public class Schema2Service {
    @Inject
    private Schema2Mapper mapper;
    
    @Transactional
    public void createSchema2() {
        mapper.createSchema2Step1();
        mapper.createSchema2Step2();
        mapper.createSchema2Step3();
    }
    
    @Transactional
    public Integer getNextValueFromSchema2() {
        return mapper.getNextValueFromSchema2();
    }
}
