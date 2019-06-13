package org.elasticsearch.extra.query.support.handler;

import org.elasticsearch.extra.context.internal.ReflectUtil;
import org.elasticsearch.extra.query.BoolQueryConverter;
import org.elasticsearch.extra.query.annotation.NestedType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;

import java.lang.reflect.Method;
import java.util.Objects;

public class NestedQueryHandler implements BoolQueryHandler {

  private BoolQueryConverter converter;
  private NestedType nestedType;
  private Method getter;

  public void setAttribute(BoolQueryConverter converter) {
    this.converter = converter;
  }

  public void setNestedType(NestedType nestedType) {
    this.nestedType = nestedType;
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
    BoolQueryBuilder inner = new BoolQueryBuilder();
    boolean flag = converter.test(inner, v);
    if (flag) {
      NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder(nestedType.path(), inner, nestedType.scoreMode());
      boolQueryBuilder.must(nestedQueryBuilder);
      return true;
    } else {
      return false;
    }
  }
}
