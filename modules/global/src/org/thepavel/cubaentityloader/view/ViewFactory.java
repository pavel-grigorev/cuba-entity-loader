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

package org.thepavel.cubaentityloader.view;

import com.haulmont.cuba.core.global.View;
import org.thepavel.cubaentityloader.annotations.QueryView;

import java.lang.reflect.Field;

public class ViewFactory {
  private static final String DEFAULT_VIEW = View.LOCAL;

  private ViewFactory() {
  }

  public static String getView(Field field) {
    if (field.isAnnotationPresent(QueryView.class)) {
      return field.getAnnotation(QueryView.class).value();
    }
    return DEFAULT_VIEW;
  }
}
