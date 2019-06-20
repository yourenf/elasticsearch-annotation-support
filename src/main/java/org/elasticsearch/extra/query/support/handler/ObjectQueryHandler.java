package org.elasticsearch.extra.query.support.handler;

import org.elasticsearch.extra.context.internal.ReflectUtil;
import org.elasticsearch.extra.query.BoolQueryConverter;
import org.elasticsearch.extra.query.annotation.NestedType;
import org.elasticsearch.extra.query.annotation.ObjectType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;

import java.lang.reflect.Method;
import java.util.Objects;

public class ObjectQueryHandler implements BoolQueryHandler {

  private BoolQueryConverter converter;
  private Method getter;

  public void setAttribute(BoolQueryConverter converter) {
    this.converter = converter;
  }

  public void setGetter(Method getter) {
    this.getter = getter;
  }

  @Override
  public boolean test(BoolQueryBuilder boolQueryBuilder, Object value) {
    Object v = ReflectUtil.getValue(getter, value);
    if (Objects.isNull(v)) {
      return false;
    }
    return converter.test(boolQueryBuilder, v);
  }
}
