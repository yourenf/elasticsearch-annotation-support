package org.elasticsearch.extra.query;

import org.elasticsearch.extra.query.plugin.converter.BeanConverterFactory;
import org.elasticsearch.extra.query.support.AnnotationSupport;

import java.util.function.Supplier;

public interface BoolQueryAttributeContext {

  /**
   * 外部注解实现
   *
   * @param supportType
   */
  void add(Class<? extends AnnotationSupport> supportType);


  <T> BoolQueryConverter<T> create(Class<T> type, String path);

  /**
   * 类型转换实现
   *
   * @return ConverterFactory
   * @see BeanConverterFactory
   */
  BeanConverterFactory getBeanConverterFactory();

  /**
   * @param factory 类型转换实现
   */
  void setBeanConverterFactory(BeanConverterFactory factory);

  /**
   * @param supplier 类型转换实现Supplier
   */
  void setBeanConverterFactory(Supplier<BeanConverterFactory> supplier);
}
