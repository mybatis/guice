/*
 *    Copyright 2010 The myBatis Team
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
package org.mybatis.guice.sample.service;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.mybatis.guice.sample.dao.UserDao;
import org.mybatis.guice.sample.domain.User;
import org.mybatis.guice.transactional.Transactional;

import javax.inject.Inject;

/**
 * Impl of the FooService.
 *
 * FooService simply receives a userId and uses a mapper/dao to get a record from the database.
 *
 * @version $Id$
 */
public class FooServiceDaoImpl implements FooService {

    private UserDao userDao;

    @Inject
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(isolationLevel = TransactionIsolationLevel.SERIALIZABLE)
    public User doSomeBusinessStuff(String userId) {
        return this.userDao.getUser(userId);
    }

}
