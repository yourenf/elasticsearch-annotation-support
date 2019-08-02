package org.elasticsearch.extra.context.annotation;

import java.lang.annotation.*;
import java.util.function.UnaryOperator;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Document {
  /**
   * 指定索引名称
   *
   * @return
   */
  String index();

  /**
   * 动态修改索引
   * - input
   * String index = "a"
   * UnaryOperator<String> indexOperator  = (x) -> x + 1
   * - output
   * String index = "a1"
   *
   * @return
   */
  Class<? extends UnaryOperator<String>> indexOperator() default NoneOperator.class;

  String type() default "_doc";

  /**
   * index.number_of_shards
   *
   * @return
   */
  int shards() default 1;

  /**
   * index.number_of_replicas
   *
   * @return
   */
  int replicas() default 0;

}