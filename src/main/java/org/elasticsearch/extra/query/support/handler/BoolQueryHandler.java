package org.elasticsearch.extra.query.support.handler;

import org.elasticsearch.index.query.BoolQueryBuilder;

import java.util.function.BiPredicate;

public interface BoolQueryHandler extends BiPredicate<BoolQueryBuilder, Object> {

  /**
   * 构建ES查询语句
   *
   * @param boolQueryBuilder
   * @param value
   * @return
   */
  boolean test(BoolQueryBuilder boolQueryBuilder, Object value);
}
