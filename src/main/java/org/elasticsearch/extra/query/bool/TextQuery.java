package org.elasticsearch.extra.query.bool;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

public class TextQuery implements StringQuery {

  @Override
  public boolean accept(final BoolQueryBuilder boolQueryBuilder, AttributeContext context, String value) {
    if (value == null) {
      return false;
    }
    String escape = QueryParser.escape(value);
    QueryStringQueryBuilder builder = new QueryStringQueryBuilder(escape);
    builder.field(context.completeField());
    builder.defaultOperator(Operator.AND);
    builder.boost(context.boost());
    boolQueryBuilder.must(builder);
    return true;
  }
}
