package org.elasticsearch.extra.query.plugin.converter.std;

import org.elasticsearch.extra.query.plugin.converter.Converter;

public class IntLikeConverter implements Converter<Object, Integer> {
  public static final IntLikeConverter INSTANCE = new IntLikeConverter();

  @Override
  public Integer apply(Object value) {
    return ((Number) value).intValue();
  }
}