package org.elasticsearch.extra.query.bool;

import org.elasticsearch.common.util.set.Sets;
import org.elasticsearch.extra.ElasticSearchClient;
import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * 关键字
 */
public class TermsQuery implements BoolQueryAttribute<Object> {
  private static final Logger log = LoggerFactory.getLogger(TermsQuery.class);

  private String separator = ",";

  @Override
  public boolean accept(final BoolQueryBuilder boolQueryBuilder, AttributeContext context, Object value) {
    if (value instanceof String) {
      String str = (String) value;
      if (str.contains(separator)) {
        Set<String> values = Sets.newHashSet();
        for (String s : str.split(separator)) {
          values.add(s.trim());
        }
        boolQueryBuilder.must(QueryBuilders.termsQuery(context.completeField(), values)).boost(context.boost());
      } else {
        boolQueryBuilder.must(QueryBuilders.termQuery(context.completeField(), value)).boost(context.boost());
      }
    } else if (value.getClass().isArray()) {
      Object[] terms = (Object[]) value;
      boolQueryBuilder.must(QueryBuilders.termsQuery(context.completeField(), terms)).boost(context.boost());
    } else if (value instanceof Collection) {
      Collection<?> collect = (Collection<?>) value;
      if (collect.isEmpty()) {
        return false;
      }
      boolQueryBuilder.must(QueryBuilders.termsQuery(context.completeField(), collect)).boost(context.boost());
    } else {
      boolQueryBuilder.must(QueryBuilders.termsQuery(context.completeField(), value)).boost(context.boost());
    }
    return true;
  }
}
