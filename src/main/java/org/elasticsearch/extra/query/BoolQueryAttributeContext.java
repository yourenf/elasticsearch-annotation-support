package org.elasticsearch.extra.query;

import org.elasticsearch.extra.query.plugin.converter.BeanConverterFactory;
import org.elasticsearch.extra.query.support.AnnotationSupport;

public interface BoolQueryAttributeContext {

  /**
   * 外部注解实现
   *
   * @param supportType
   */
  void add(Class<? extends AnnotationSupport> supportType);


  <T> BoolQueryConverter<T> create(Class<T> type, String path);

  /**
   * @see BeanConverterFactory
   *
   * @return ConverterFactory
   */
  BeanConverterFactory getFactory();
}
