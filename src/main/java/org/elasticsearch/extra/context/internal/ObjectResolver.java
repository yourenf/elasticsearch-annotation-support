package org.elasticsearch.extra.context.internal;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public enum ObjectResolver {
  INSTANCE;

  private final ConcurrentMap<Class<?>, List<Property>> types = new ConcurrentHashMap<>();

  public List<Property> getProperties(final Class<?> type) {
    Objects.requireNonNull(type);
    return types.computeIfAbsent(type, k -> {
      List<Property> list = resolve(k);
      return Collections.unmodifiableList(list);
    });
  }

  private List<Property> resolve(final Class<?> clazz) {
    try {
      Field[] fields = clazz.getDeclaredFields();
      Map<String, Field> map = Arrays.stream(fields).collect(Collectors.toMap(Field::getName, t -> t));
      List<Property> properties = new ArrayList<>(fields.length);
      PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(clazz)
              .getPropertyDescriptors();
      for (PropertyDescriptor descriptor : propertyDescriptors) {
        if (Objects.equals(descriptor.getName(), "class")) {
          continue;
        }
        Field field = map.get(descriptor.getName());
        Property property = new Property(descriptor, field);
        properties.add(property);
      }
      return properties;
    } catch (IntrospectionException e) {
      throw new IllegalArgumentException(e);
    }
  }


}
