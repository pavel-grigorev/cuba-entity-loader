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

package org.thepavel.cubaentityloader.query;

import org.thepavel.cubaentityloader.queryparams.ParamsSupplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class QueryRunnerParams {
  private final List<ParamsSupplier> paramsSuppliers;
  private final Map<String, Object> customParams = new HashMap<>();

  QueryRunnerParams(List<ParamsSupplier> paramsSuppliers) {
    this.paramsSuppliers = paramsSuppliers;
  }

  void add(String name, Object value) {
    customParams.put(name, value);
  }

  void add(Map<String, Object> params) {
    customParams.putAll(params);
  }

  Map<String, Object> get() {
    Map<String, Object> result = new HashMap<>();
    collectFromSuppliers(result);
    result.putAll(customParams);
    return result;
  }

  private void collectFromSuppliers(Map<String, Object> result) {
    paramsSuppliers
        .stream()
        .map(ParamsSupplier::getParams)
        .forEach(result::putAll);
  }
}
