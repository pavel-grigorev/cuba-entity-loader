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

package org.thepavel.cubaentityloader;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import org.springframework.stereotype.Component;
import org.thepavel.cubaentityloader.annotations.Secure;
import org.thepavel.cubaentityloader.query.QueryFactory;
import org.thepavel.cubaentityloader.queryparams.ParamsSupplier;
import org.thepavel.cubaentityloader.queryparams.ParamsSuppliersFactory;
import org.thepavel.cubaentityloader.utils.ReflectionHelper;
import org.thepavel.cubaentityloader.view.ViewFactory;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.List;

@Component
public class EntityLoaderFactory {
  private final DataManager dataManager;
  private final QueryFactory queryFactory;
  private final ParamsSuppliersFactory paramsSuppliersFactory;

  @Inject
  public EntityLoaderFactory(DataManager dataManager, QueryFactory queryFactory, ParamsSuppliersFactory paramsSuppliersFactory) {
    this.dataManager = dataManager;
    this.queryFactory = queryFactory;
    this.paramsSuppliersFactory = paramsSuppliersFactory;
  }

  public <T extends Entity<K>, K> EntityLoader<T, K> createEntityLoader(FieldContext fieldContext) {
    Field field = fieldContext.getField();

    DataManager dataManager = getDataManager(field);
    Class<T> entityClass = getEntityClass(field);
    String view = ViewFactory.getView(field);
    String query = getQuery(field, entityClass);
    List<ParamsSupplier> paramsSuppliers = getParamsSuppliers(fieldContext);

    return new EntityLoader<>(dataManager, entityClass, view, query, paramsSuppliers);
  }

  private DataManager getDataManager(Field field) {
    boolean isSecure = field.isAnnotationPresent(Secure.class);
    return isSecure ? dataManager.secure() : dataManager;
  }

  @SuppressWarnings("unchecked")
  private static <T extends Entity<K>, K> Class<T> getEntityClass(Field field) {
    return (Class<T>) ReflectionHelper.getGenericTypes(field)[0];
  }

  private String getQuery(Field field, Class<?> entityClass) {
    return queryFactory.getQuery(field, entityClass);
  }

  private List<ParamsSupplier> getParamsSuppliers(FieldContext fieldContext) {
    return paramsSuppliersFactory.getParamsSuppliers(fieldContext);
  }
}
