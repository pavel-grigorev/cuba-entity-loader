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

package org.thepavel.cubaentityloader.utils;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionHelper {
  private ReflectionHelper() {
  }

  public static Object invokeMethod(String name, Object instance) {
    Class<?> type = instance.getClass();
    Method method = ReflectionUtils.findMethod(type, name);

    if (method == null) {
      throw new IllegalArgumentException("Class " + type + " has no method " + name);
    }

    makeAccessible(method);
    return ReflectionUtils.invokeMethod(method, instance);
  }

  public static void setValue(Field field, Object instance, Object value) {
    makeAccessible(field);
    ReflectionUtils.setField(field, instance, value);
  }

  private static void makeAccessible(AccessibleObject object) {
    if (!object.isAccessible()) {
      object.setAccessible(true);
    }
  }

  public static Class<?>[] getGenericTypes(Field field) {
    Type fieldType = field.getGenericType();

    if (!(fieldType instanceof ParameterizedType)) {
      throw new IllegalArgumentException("Generic type is not specified for the field " + field.getName());
    }

    ParameterizedType type = (ParameterizedType) fieldType;
    return toClass(type.getActualTypeArguments());
  }

  private static Class<?>[] toClass(Type[] types) {
    Class<?>[] classes = new Class[types.length];
    for (int i = 0; i < types.length; i++) {
      classes[i] = (Class<?>) types[i];
    }
    return classes;
  }
}
