package org.elasticsearch.extra.context.annotation;

import java.lang.annotation.*;

/**
 * _routing field
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Routing {

}