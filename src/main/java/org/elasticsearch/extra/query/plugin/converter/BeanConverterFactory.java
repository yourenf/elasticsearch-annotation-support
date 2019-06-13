package org.elasticsearch.extra.query.plugin.converter;

import org.elasticsearch.extra.query.plugin.converter.std.CollectionConverter;

import java.util.Collection;

public class BeanConverterFactory extends BasicConverterFactory {

  @Override
  public Converter<?, ?> findConverter(Class<?> type) {
    if (Collection.class.isAssignableFrom(type)) {
      return new CollectionConverter(this);
    }
    return concrete.getOrDefault(type.getName(),RawConverter.INSTANCE);
  }
}
