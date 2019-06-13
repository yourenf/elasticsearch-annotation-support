package org.elasticsearch.extra.query.plugin.converter;

import org.elasticsearch.extra.query.plugin.converter.std.IntLikeConverter;
import org.elasticsearch.extra.query.plugin.converter.std.NumberConverter;
import org.elasticsearch.extra.query.plugin.converter.std.StringTrimToNullConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BasicConverterFactory {
  protected final Map<String, Converter<?, ?>> concrete;

  protected BasicConverterFactory() {
    concrete = new ConcurrentHashMap<>();
    concrete.put(String.class.getName(), StringTrimToNullConverter.INSTANCE);
    concrete.put(Byte.class.getName(), IntLikeConverter.INSTANCE);
    concrete.put(Byte.TYPE.getName(), IntLikeConverter.INSTANCE);
    concrete.put(BigInteger.class.getName(), NumberConverter.INSTANCE);
    concrete.put(BigDecimal.class.getName(), NumberConverter.INSTANCE);
  }

  public final BasicConverterFactory add(String className, Converter<?, ?> converter) {
    concrete.put(className, converter);
    return this;
  }

  public abstract Converter<?, ?> findConverter(Class<?> type);

}
