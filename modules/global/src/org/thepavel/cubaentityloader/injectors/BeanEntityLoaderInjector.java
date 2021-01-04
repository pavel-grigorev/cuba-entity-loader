/*
 * Copyright (c) 2020-2021 Pavel Grigorev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.thepavel.cubaentityloader.injectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class BeanEntityLoaderInjector implements BeanPostProcessor {
  private final EntityLoaderInjector entityLoaderInjector;

  @Inject
  public BeanEntityLoaderInjector(EntityLoaderInjector entityLoaderInjector) {
    this.entityLoaderInjector = entityLoaderInjector;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    entityLoaderInjector.inject(bean);
    return bean;
  }
}
