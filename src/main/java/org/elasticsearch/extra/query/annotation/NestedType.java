package org.elasticsearch.extra.query.annotation;

import org.apache.lucene.search.join.ScoreMode;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface NestedType {

  /**
   * nested 查询 path
   *
   * @return
   */
  String path();


  ScoreMode scoreMode() default ScoreMode.None;

}
