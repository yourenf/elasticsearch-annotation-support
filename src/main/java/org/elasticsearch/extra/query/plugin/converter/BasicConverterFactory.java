package org.elasticsearch.extra.query.plugin.converter;

import org.elasticsearch.extra.query.plugin.converter.std.IntLikeConverter;
import org.elasticsearch.extra.query.plugin.converter.std.NumberConverter;
import org.elasticsearch.extra.query.plugin.converter.std.StringTrimToNullConverter;
import org.elasticsearch.extra.query.plugin.converter.std.ToStringConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BasicConverterFactory {
  protected final Map<String, Converter<?, ?>> concrete;
  protected final Map<String, ConverterFactory> concreteFactory;
  private static final ClassHierarchy classHierarchy = new ClassHierarchy();

  protected BasicConverterFactory() {
    concrete = new ConcurrentHashMap<>();
    concrete.put(String.class.getName(), StringTrimToNullConverter.INSTANCE);
    concrete.put(Byte.class.getName(), IntLikeConverter.INSTANCE);
    concrete.put(Byte.TYPE.getName(), IntLikeConverter.INSTANCE);
    concrete.put(BigInteger.class.getName(), NumberConverter.INSTANCE);
    concrete.put(BigDecimal.class.getName(), NumberConverter.INSTANCE);
    concrete.put(LocalDateTime.class.getName(), ToStringConverter.INSTANCE);
    concrete.put(LocalDate.class.getName(), ToStringConverter.INSTANCE);

    concreteFactory = new ConcurrentHashMap<>();
  }

  public final <S, R> BasicConverterFactory addConverterFactory(Class<?> type, ConverterFactory<S, R> converterFactory) {
    concreteFactory.put(type.getName(), converterFactory);
    return this;
  }

  public final BasicConverterFactory addConverter(Class<?> type, Converter<?, ?> converter) {
    concrete.put(type.getName(), converter);
    return this;
  }

  public <T extends S, S> Converter<S, ?> findConverter(Class<T> type) {
    Objects.requireNonNull(type);
    Converter<?, ?> converter = concrete.get(type.getName());
    if (Objects.nonNull(converter)) {
      return (Converter<S, ?>) converter;
    }
    List<Class<?>> list = classHierarchy.getClassHierarchy(type);
    for (Class<?> superClass : list) {
      ConverterFactory factory = concreteFactory.get(superClass.getName());
      if (Objects.nonNull(factory)) {
        Converter converter1 = factory.getConverter(type);
        concrete.put(type.getName(), converter1);
        return converter1;
      }
    }
    return null;
  }

}