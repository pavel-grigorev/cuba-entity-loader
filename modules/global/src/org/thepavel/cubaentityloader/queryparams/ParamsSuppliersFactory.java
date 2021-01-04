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

package org.thepavel.cubaentityloader.queryparams;

import org.springframework.stereotype.Component;
import org.thepavel.cubaentityloader.FieldContext;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParamsSuppliersFactory {
  private final List<ParamsSupplierFactory> factories;

  @Inject
  public ParamsSuppliersFactory(List<ParamsSupplierFactory> factories) {
    this.factories = factories;
  }

  public List<ParamsSupplier> getParamsSuppliers(FieldContext fieldContext) {
    return factories
        .stream()
        .filter(f -> f.isApplicable(fieldContext))
        .map(f -> f.createParamsSupplier(fieldContext))
        .collect(Collectors.toList());
  }
}
