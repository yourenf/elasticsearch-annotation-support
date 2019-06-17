package org.elasticsearch.extra.query.plugin.converter;

public interface ConverterFactory<S, R> {

  <T extends S> Converter<S, R> getConverter(Class<T> targetType);

}
