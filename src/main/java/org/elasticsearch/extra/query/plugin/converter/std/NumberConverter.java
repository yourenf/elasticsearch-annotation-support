package org.elasticsearch.extra.query.plugin.converter.std;

import org.elasticsearch.extra.query.plugin.converter.Converter;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberConverter implements Converter<Number, Object> {

  public static final NumberConverter INSTANCE = new NumberConverter();

  @Override
  public Object apply(Number value) {
    if (value instanceof BigDecimal) {
      return value;
    } else if (value instanceof BigInteger) {
      return value;
    } else if (value instanceof Long) {
      return value.longValue();
    } else if (value instanceof Double) {
      return value.doubleValue();
    } else if (value instanceof Float) {
      return value.floatValue();
    } else if (value instanceof Integer || value instanceof Byte || value instanceof Short) {
      return value.intValue();
    } else {
      return value.toString();
    }
  }
}
