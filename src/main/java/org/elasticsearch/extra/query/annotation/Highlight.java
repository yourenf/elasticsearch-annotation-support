package org.elasticsearch.extra.query.annotation;

import java.lang.annotation.*;

/**
 * HighlightBuilder 参数
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Highlight {
	String[] field();

	String preTags() default "<em>";

	String postTags() default "</em>";
}
