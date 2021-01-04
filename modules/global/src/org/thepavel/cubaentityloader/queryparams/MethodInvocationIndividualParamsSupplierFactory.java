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

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.thepavel.cubaentityloader.FieldContext;
import org.thepavel.cubaentityloader.annotations.QueryParam;
import org.thepavel.cubaentityloader.annotations.QueryParamList;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Component
@Order(1)
public class MethodInvocationIndividualParamsSupplierFactory implements ParamsSupplierFactory {
  @Override
  public ParamsSupplier createParamsSupplier(FieldContext fieldContext) {
    Map<String, String> paramsToMethodsMap = getParamsToMethodsMap(fieldContext);
    return new MethodInvocationIndividualParamsSupplier(paramsToMethodsMap, fieldContext.getInstance());
  }

  private static Map<String, String> getParamsToMethodsMap(FieldContext fieldContext) {
    Field field = fieldContext.getField();

    return Arrays
        .stream(field.getAnnotationsByType(QueryParam.class))
        .collect(toMap(QueryParam::name, QueryParam::supplier));
  }

  @Override
  public boolean isApplicable(FieldContext fieldContext) {
    Field field = fieldContext.getField();

    return field.isAnnotationPresent(QueryParam.class) ||
        field.isAnnotationPresent(QueryParamList.class);
  }
}
