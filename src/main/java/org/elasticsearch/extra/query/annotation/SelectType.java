package org.elasticsearch.extra.query.annotation;


import org.elasticsearch.extra.query.bool.BoolQueryAttribute;
import org.elasticsearch.extra.query.bool.TermsQuery;
import org.elasticsearch.extra.query.plugin.strategy.EndWithStrategy;
import org.elasticsearch.extra.query.plugin.strategy.Strategy;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Repeatable(SelectType.List.class)
public @interface SelectType {
  /**
   * 检索字段名
   */
  String field();

  /**
   * 检索实现类型
   */
  Class<? extends BoolQueryAttribute<?>> attribute() default TermsQuery.class;


  float boost() default 1.0f;

  /**
   * 策略条件
   */
  String rule() default "";


  /**
   * 策略实现
   */
  Class<? extends Strategy> strategy() default EndWithStrategy.class;

  @Target({FIELD})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    SelectType[] value();
  }
}
