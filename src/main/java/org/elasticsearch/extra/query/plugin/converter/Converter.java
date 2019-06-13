package org.elasticsearch.extra.query.plugin.converter;

import java.util.function.Function;

public interface Converter<T, R> extends Function<T, R> {

  R apply(T value);
}