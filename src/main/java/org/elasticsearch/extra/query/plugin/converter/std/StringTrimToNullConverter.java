package org.elasticsearch.extra.query.plugin.converter.std;

import org.elasticsearch.extra.query.plugin.converter.Converter;

import java.util.Objects;

/**
 * null -> null
 * ""  -> null
 * " "  -> null
 * "a "  -> "a"
 * "a"  -> "a"
 */
public class StringTrimToNullConverter implements Converter<String, String> {
  public static final StringTrimToNullConverter INSTANCE = new StringTrimToNullConverter();

  @Override
  public String apply(String value) {
    if (Objects.isNull(value)) {
      return null;
    }
    String v = value.trim();
    return v.isEmpty() ? null : v;
  }
}