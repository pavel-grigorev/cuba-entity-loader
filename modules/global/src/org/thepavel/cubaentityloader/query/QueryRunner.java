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

package org.thepavel.cubaentityloader.query;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.core.global.LoadContext;
import org.thepavel.cubaentityloader.queryparams.ParamsSupplier;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QueryRunner<T extends Entity<K>, K> {
  private final DataManager dataManager;
  private final Class<T> entityClass;
  private String view;
  private final String query;
  private final QueryRunnerParams params;

  public QueryRunner(DataManager dataManager, Class<T> entityClass, String view, String query, List<ParamsSupplier> paramsSuppliers) {
    this.dataManager = dataManager;
    this.entityClass = entityClass;
    this.view = view;
    this.query = query;
    this.params = new QueryRunnerParams(paramsSuppliers);
  }

  public List<T> list() {
    return byQueryLoader().list();
  }

  public List<T> page(int firstResult, int maxResults) {
    return byQueryLoader()
        .firstResult(firstResult)
        .maxResults(maxResults)
        .list();
  }

  public T one() {
    return byQueryLoader().one();
  }

  public Optional<T> optional() {
    return byQueryLoader().optional();
  }

  private FluentLoader.ByQuery<T, K> byQueryLoader() {
    return dataManager
        .load(entityClass)
        .view(view)
        .query(query)
        .setParameters(params.get());
  }

  public T one(K id) {
    return byIdLoader(id).one();
  }

  public Optional<T> optional(K id) {
    return byIdLoader(id).optional();
  }

  private FluentLoader.ById<T, K> byIdLoader(K id) {
    return dataManager
        .load(entityClass)
        .view(view)
        .id(id);
  }

  public long count() {
    LoadContext.Query query = LoadContext
        .createQuery(this.query)
        .setParameters(params.get());

    LoadContext<T> context = LoadContext
        .create(entityClass)
        .setView(view)
        .setQuery(query);

    return dataManager.getCount(context);
  }

  public QueryRunner<T, K> view(String view) {
    this.view = view;
    return this;
  }

  public QueryRunner<T, K> param(String name, Object value) {
    params.add(name, value);
    return this;
  }

  public QueryRunner<T, K> params(Map<String, Object> params) {
    this.params.add(params);
    return this;
  }
}
