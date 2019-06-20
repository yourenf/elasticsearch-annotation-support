package org.elasticsearch.extra.query.annotation;

import java.lang.annotation.*;

/**
 * 对象查询
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ObjectType {

  String field();

}
