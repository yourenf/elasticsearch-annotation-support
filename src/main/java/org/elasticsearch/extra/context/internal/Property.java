package org.elasticsearch.extra.context.internal;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

public class Property {
  private final Field field;
  private final PropertyDescriptor propertyDescriptor;

  public Property(PropertyDescriptor propertyDescriptor, Field field) {
    this.field = field;
    this.propertyDescriptor = Objects.requireNonNull(propertyDescriptor);
  }

  public Class<?> getType() {
    return propertyDescriptor.getPropertyType();
  }

  public String getName() {
    return propertyDescriptor.getName();
  }

  public Field getField() {
    return field;
  }

  public <T extends Annotation> T getAnnotation(final Class<T> annotationCls) {
    return Optional.ofNullable(propertyDescriptor.getReadMethod().getAnnotation(annotationCls))
            .orElseGet(() -> {
              if (Objects.isNull(field)) {
                return null;
              }
              return field.getAnnotation(annotationCls);
            });
  }

  public Method getReadMethod() {
    return propertyDescriptor.getReadMethod();
  }

  public Method getWriteMethod() {
    return propertyDescriptor.getWriteMethod();
  }

  @Override
  public String toString() {
    return "Property{" +
            "field=" + propertyDescriptor.getName() +
            ", propertyType=" + propertyDescriptor.getPropertyType().toString() +
            '}';
  }
}
