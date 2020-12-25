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

package org.thepavel.cubaentityloader;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import org.thepavel.cubaentityloader.query.QueryRunner;
import org.thepavel.cubaentityloader.queryparams.ParamsSupplier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EntityLoader<T extends Entity<K>, K> {
  private final DataManager dataManager;
  private final Class<T> entityClass;
  private final String view;
  private final String query;
  private final List<ParamsSupplier> paramsSuppliers;

  public EntityLoader(DataManager dataManager, Class<T> entityClass, String view, String query, List<ParamsSupplier> paramsSuppliers) {
    this.dataManager = dataManager;
    this.entityClass = entityClass;
    this.view = view;
    this.query = query;
    this.paramsSuppliers = paramsSuppliers;
  }

  public List<T> list() {
    return queryRunner().list();
  }

  public List<T> page(int firstResult, int maxResults) {
    return queryRunner().page(firstResult, maxResults);
  }

  public T one() {
    return queryRunner().one();
  }

  public Optional<T> optional() {
    return queryRunner().optional();
  }

  public T one(K id) {
    return queryRunner().one(id);
  }

  public Optional<T> optional(K id) {
    return queryRunner().optional(id);
  }

  public long count() {
    return queryRunner().count();
  }

  public QueryRunner<T, K> view(String view) {
    return queryRunner().view(view);
  }

  public QueryRunner<T, K> param(String name, Object value) {
    return queryRunner().param(name, value);
  }

  public QueryRunner<T, K> params(Map<String, Object> params) {
    return queryRunner().params(params);
  }

  private QueryRunner<T, K> queryRunner() {
    return new QueryRunner<>(dataManager, entityClass, view, query, paramsSuppliers);
  }
}
