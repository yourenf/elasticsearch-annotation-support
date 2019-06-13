package org.elasticsearch.extra.query.bool;

import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.index.query.BoolQueryBuilder;

public interface BoolQueryAttribute<T> {

  boolean accept(final BoolQueryBuilder boolQueryBuilder, AttributeContext context, T value);

}
