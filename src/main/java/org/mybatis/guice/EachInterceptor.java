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
package org.mybatis.guice;

import org.apache.ibatis.plugin.Interceptor;

import com.google.inject.Binder;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

/**
 * 
 *
 * @version $Id$
 */
final class EachInterceptor extends AbstractBinderEach<Class<? extends Interceptor>> {

    private Multibinder<Interceptor> interceptorsMultibinder;

    public EachInterceptor(final Binder binder) {
        super(binder);
    }

    public void init() {
        this.interceptorsMultibinder = Multibinder.newSetBinder(this.getBinder(), Interceptor.class);
    }

    public void doHandle(Class<? extends Interceptor> interceptorType) {
        this.interceptorsMultibinder.addBinding().to(interceptorType).in(Scopes.SINGLETON);
    }

}
