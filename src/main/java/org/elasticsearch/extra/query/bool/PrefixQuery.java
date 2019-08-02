package org.elasticsearch.extra.query.bool;

import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

public class PrefixQuery implements StringQuery {

  @Override
  public boolean accept(final BoolQueryBuilder boolQueryBuilder, AttributeContext context, final String value) {
    PrefixQueryBuilder builder = QueryBuilders.prefixQuery(context.completeField(), value).boost(context.boost());
    boolQueryBuilder.must(builder);
    return true;
  }
}
