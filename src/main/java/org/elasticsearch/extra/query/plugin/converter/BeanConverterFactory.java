package org.elasticsearch.extra.query.plugin.converter;

import org.elasticsearch.extra.query.plugin.converter.std.CollectionConverter;

import java.util.Collection;
import java.util.Objects;

public class BeanConverterFactory extends BasicConverterFactory {

  @Override
  public <T extends S, S> Converter<S, ?> findConverter(Class<T> type) {
    if (Collection.class.isAssignableFrom(type)) {
      new CollectionConverter(this);
    }
    Converter<S, ?> converter = super.findConverter(type);
    if (Objects.nonNull(converter)) {
      return converter;
    }
    return (Converter<S, ?>) RawConverter.INSTANCE;
  }
}
