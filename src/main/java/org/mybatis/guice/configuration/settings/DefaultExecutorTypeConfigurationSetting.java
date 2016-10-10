/**
 *    Copyright 2009-2016 the original author or authors.
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
package org.mybatis.guice.configuration.settings;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;

public class DefaultExecutorTypeConfigurationSetting implements ConfigurationSetting  {

	private final ExecutorType executorType;
	
	public DefaultExecutorTypeConfigurationSetting(final ExecutorType executorType) {
		this.executorType = executorType;
	}

	@Override
	public void applyConfigurationSetting(Configuration configuration) {
		configuration.setDefaultExecutorType(executorType);
	}
}
