package org.elasticsearch.extra.query;

import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public class BoolQueryConverter<T> implements BiPredicate<BoolQueryBuilder, T> {
  private List<BoolQueryHandler> handlers;

  public BoolQueryConverter(List<BoolQueryHandler> handlers) {
    this.handlers = handlers;
  }

  @Override
  public boolean test(BoolQueryBuilder boolQueryBuilder, T value) {
    Objects.requireNonNull(value);
    Objects.requireNonNull(boolQueryBuilder);
    int write = 0;
    for (BoolQueryHandler handler : handlers) {
      boolean flag = handler.test(boolQueryBuilder, value);
      if (flag) {
        write++;
      }
    }
    return write > 0;
  }
}
