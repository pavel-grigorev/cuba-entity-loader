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

package org.thepavel.cubaentityloader.queryparams;

import org.thepavel.cubaentityloader.utils.ReflectionHelper;

import java.util.HashMap;
import java.util.Map;

public class MethodInvocationIndividualParamsSupplier implements ParamsSupplier {
  private final Map<String, String> paramsToMethodsMap;
  private final Object instance;

  public MethodInvocationIndividualParamsSupplier(Map<String, String> paramsToMethodsMap, Object instance) {
    this.paramsToMethodsMap = paramsToMethodsMap;
    this.instance = instance;
  }

  @Override
  public Map<String, Object> getParams() {
    // Collectors#toMap() is not used here
    // because it does not support null values
    Map<String, Object> result = new HashMap<>();

    paramsToMethodsMap.forEach((paramName, methodName) -> {
      Object value = ReflectionHelper.invokeMethod(methodName, instance);
      result.put(paramName, value);
    });

    return result;
  }
}
