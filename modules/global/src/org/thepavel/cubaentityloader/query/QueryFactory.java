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

import com.haulmont.cuba.core.global.MetadataTools;
import org.springframework.stereotype.Component;
import org.thepavel.cubaentityloader.annotations.Query;

import javax.inject.Inject;
import java.lang.reflect.Field;

@Component
public class QueryFactory {
  private static final String DEFAULT_QUERY = "select e from %s e";

  private final MetadataTools metadataTools;

  @Inject
  public QueryFactory(MetadataTools metadataTools) {
    this.metadataTools = metadataTools;
  }

  public String getQuery(Field field, Class<?> entityClass) {
    if (field.isAnnotationPresent(Query.class)) {
      return field.getAnnotation(Query.class).value();
    }
    return getDefaultQuery(entityClass);
  }

  private String getDefaultQuery(Class<?> entityClass) {
    return String.format(DEFAULT_QUERY, getEntityName(entityClass));
  }

  private String getEntityName(Class<?> entityClass) {
    return metadataTools.getEntityName(entityClass);
  }
}
