package org.elasticsearch.extra.query.plugin.converter;

/**
 * 不做任何处理
 */
public class RawConverter implements Converter<Object, Object> {

  public static final RawConverter INSTANCE = new RawConverter();

  @Override
  public Object apply(Object value) {
    return value;
  }
}
