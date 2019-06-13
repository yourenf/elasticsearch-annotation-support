package org.elasticsearch.extra.query.plugin.converter.std;

import org.elasticsearch.extra.query.plugin.converter.BeanConverterFactory;
import org.elasticsearch.extra.query.plugin.converter.Converter;

import java.util.Collection;
import java.util.stream.Collectors;

public class CollectionConverter implements AsArrayConverter<Collection<?>, Collection<?>> {

  private BeanConverterFactory factory;

  public CollectionConverter(BeanConverterFactory factory) {
    this.factory = factory;
  }

  @Override
  public Collection<?> apply(Collection<?> value) {
    if (value == null || value.isEmpty()) {
      return value;
    }
    return value.stream().map(v -> {
      Converter converter = factory.findConverter(v.getClass());
      return converter.apply(v);
    }).collect(Collectors.toList());
  }
}
