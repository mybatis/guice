/*
 *    Copyright 2010-2012 The MyBatis Team
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

import org.mybatis.guice.sample.domain.User;
import org.mybatis.guice.sample.mapper.UserMapper;
import org.mybatis.guice.transactional.Isolation;
import org.mybatis.guice.transactional.Transactional;

import javax.inject.Inject;

/**
 * Impl of the FooService.
 *
 * FooService simply receives a userId and uses a mapper/dao to get a record from the database.
 *
 * @version $Id$
 */
@Transactional(isolation = Isolation.SERIALIZABLE)
public class FooServiceMapperImpl implements FooService {

    private UserMapper userMapper;

    @Inject
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User doSomeBusinessStuff(String userId) {
        return this.userMapper.getUser(userId);
    }

}
