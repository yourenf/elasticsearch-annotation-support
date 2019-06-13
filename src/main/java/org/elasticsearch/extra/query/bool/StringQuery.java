package org.elasticsearch.extra.query.bool;

import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.index.query.BoolQueryBuilder;

public interface StringQuery extends BoolQueryAttribute<String> {

  @Override
  boolean accept(final BoolQueryBuilder boolQueryBuilder, AttributeContext context, String value);
}