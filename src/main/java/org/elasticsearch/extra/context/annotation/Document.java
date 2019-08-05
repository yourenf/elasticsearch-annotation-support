package org.elasticsearch.extra.context.annotation;

import java.lang.annotation.*;

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