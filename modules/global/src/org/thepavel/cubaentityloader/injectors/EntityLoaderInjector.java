/*
 * Copyright (c) 2020 Pavel Grigorev.
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

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.thepavel.cubaentityloader.EntityLoader;
import org.thepavel.cubaentityloader.EntityLoaderFactory;
import org.thepavel.cubaentityloader.FieldContext;
import org.thepavel.cubaentityloader.utils.ReflectionHelper;

import javax.inject.Inject;
import java.lang.reflect.Field;

@Component
public class EntityLoaderInjector {
  private final EntityLoaderFactory entityLoaderFactory;

  @Inject
  public EntityLoaderInjector(EntityLoaderFactory entityLoaderFactory) {
    this.entityLoaderFactory = entityLoaderFactory;
  }

  public void inject(Object instance) {
    ReflectionUtils.doWithFields(
        instance.getClass(),
        field -> assignEntityLoader(field, instance),
        this::isEntityLoaderField
    );
  }

  private void assignEntityLoader(Field field, Object instance) {
    ReflectionHelper.setValue(field, instance, createEntityLoader(field, instance));
  }

  private Object createEntityLoader(Field field, Object instance) {
    return entityLoaderFactory.createEntityLoader(new FieldContext(field, instance));
  }

  private boolean isEntityLoaderField(Field field) {
    return field.getType().equals(EntityLoader.class);
  }
}
