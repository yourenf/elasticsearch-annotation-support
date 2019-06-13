package org.elasticsearch.extra.query.bool;

import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

/**
 * 关键字
 */
public class TermQuery implements BoolQueryAttribute<Object> {

  @Override
  public boolean accept(BoolQueryBuilder boolQueryBuilder, AttributeContext context, Object value) {
    TermQueryBuilder builder = QueryBuilders.termQuery(context.completeField(), value).boost(context.boost());
    boolQueryBuilder.must(builder);
    return true;
  }
}
