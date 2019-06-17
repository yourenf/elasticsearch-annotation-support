package org.elasticsearch.extra.query.plugin.converter.std;

import org.elasticsearch.extra.query.plugin.converter.Converter;

public class ToStringConverter implements Converter<Object, String> {
  public static final ToStringConverter INSTANCE = new ToStringConverter();

  @Override
  public String apply(Object value) {
    return value.toString();
  }
}
