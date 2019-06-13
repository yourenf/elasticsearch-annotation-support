package org.elasticsearch.extra.query.annotation;


import org.elasticsearch.extra.query.bool.BoolQueryAttribute;
import org.elasticsearch.extra.query.bool.TermQuery;

import java.lang.annotation.*;

/**
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface QueryType {
  /**
   * 检索字段名
   */
  String field();

  /**
   * 检索实现类型
   */
  Class<? extends BoolQueryAttribute<?>> attribute() default TermQuery.class;

  float boost() default 1.0f;
}
