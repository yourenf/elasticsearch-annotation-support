package org.elasticsearch.extra.query.annotation;

import java.lang.annotation.*;

/**
 * CollapseBuilder 参数
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Collapse {
  String field();

  String innerHitsName() default "collapsed_hits";

  int innerHitsForm() default 0;

  int innerHitSize() default 0;
}
