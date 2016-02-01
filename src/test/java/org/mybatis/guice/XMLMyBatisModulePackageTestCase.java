/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 *
 * @version $Id: XMLMyBatisModuleTestCase.java 3980 2011-10-24 10:54:01Z simone.tripodi $
 */
@RunWith(XMLGuicePackageTestRunner.class)
public final class XMLMyBatisModulePackageTestCase extends AbstractMyBatisModuleTestCase {

    @Inject
    @Named("contactWithAddress")
    private Contact contactWithAdress;

    @Inject
    private ContactMapperClient contactMapperClient;

    @Inject
    private AddressConverter addressConverter;

    @Inject
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void testEnvironmentId() throws Exception {
        assert "test".equals(sqlSessionFactory.getConfiguration().getEnvironment().getId());
    }

    @Test
    public void testAddressConverter() throws Exception {
        Address address = new Address();
        address.setNumber(1234);
        address.setStreet("Elm street");
        assert "1234 Elm street".equals(addressConverter.convert(address));
        assert address.equals(addressConverter.convert("1234 Elm street"));
    }

    @Test
    public void insertContactWithAddress() throws Exception {
        Address address = new Address();
        address.setNumber(1234);
        address.setStreet("Elm street");
        this.contactWithAdress.setAddress(address);
        this.contactMapperClient.insert(this.contactWithAdress);
    }

    @Test
    public void selectContactWithAddress() throws Exception {
        Contact contact = this.contactMapperClient.selectById(this.contactWithAdress.getId());
        assert contact != null : "impossible to retrieve Contact with id '"
                                + this.contactWithAdress.getId()
                                + "'";
        assert this.contactWithAdress.equals(contact) : "Expected "
                                                + this.contactWithAdress
                                                + " but found "
                                                + contact;
    }

}
