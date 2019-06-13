package org.elasticsearch.extra.query.support.handler;

import org.elasticsearch.extra.context.internal.ReflectUtil;
import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.extra.query.bool.BoolQueryAttribute;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.lang.reflect.Method;
import java.util.Objects;

public class RawQueryHandler extends AbstractQueryHandler {

  private AttributeContext context;
  private BoolQueryAttribute attribute;
  private Method getter;

  public void setContext(AttributeContext context) {
    this.context = context;
  }

  public void setAttribute(BoolQueryAttribute attribute) {
    this.attribute = attribute;
  }

  public void setGetter(Method getter) {
    this.getter = getter;
  }

  @Override
  public boolean support(Class<?> handlerType) {
    return true;
  }

  @Override
  public boolean test(BoolQueryBuilder boolQueryBuilder, Object value) {
    Object v = ReflectUtil.getValue(getter, value);
    if (Objects.isNull(v)) {
      return false;
    }
    Object handlerValue = converter.apply(v);
    return attribute.accept(boolQueryBuilder, context, handlerValue);
  }
}
