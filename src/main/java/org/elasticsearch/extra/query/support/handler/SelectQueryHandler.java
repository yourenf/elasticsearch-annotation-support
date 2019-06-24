package org.elasticsearch.extra.query.support.handler;

import org.elasticsearch.extra.context.internal.ReflectUtil;
import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.extra.query.bool.BoolQueryAttribute;
import org.elasticsearch.extra.query.plugin.strategy.Strategy;
import org.elasticsearch.index.query.BoolQueryBuilder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

public class SelectQueryHandler implements BoolQueryHandler {

  private List<StringQueryHandler> strategyHandlers;
  private Method getter;

  public void setStrategyHandlers(List<StringQueryHandler> strategyHandlers) {
    this.strategyHandlers = strategyHandlers;
  }

  public void setGetter(Method getter) {
    this.getter = getter;
  }

  @Override
  public boolean test(BoolQueryBuilder boolQueryBuilder, Object value) {
    Object currentValue = ReflectUtil.getValue(getter, value);
    if (Objects.isNull(currentValue)) {
      return false;
    }
    for (StringQueryHandler handler : strategyHandlers) {
      boolean success = handler.test(boolQueryBuilder, currentValue);
      if (success) {
        return true;
      }
    }
    return false;
  }

  public static class StringQueryHandler extends AbstractQueryHandler {
    private Strategy strategy;
    private BoolQueryAttribute attribute;
    private AttributeContext context;

    @Override
    public boolean support(Class<?> handlerType) {
      return Objects.equals(String.class, handlerType);
    }

    public void setStrategy(Strategy strategy) {
      this.strategy = strategy;
    }

    public void setAttribute(BoolQueryAttribute attribute) {
      this.attribute = attribute;
    }

    public void setContext(AttributeContext context) {
      this.context = context;
    }

    @Override
    public boolean test(BoolQueryBuilder boolQueryBuilder, Object value) {
      String strValue = (String) value;
      if (strategy.match(strValue)) {
        String cleanValue = strategy.cleanValue(strValue);
        Object handlerValue = converter.apply(cleanValue);
        if (Objects.isNull(handlerValue)) {
          return false;
        }
        return attribute.accept(boolQueryBuilder, context, handlerValue);
      }
      return false;
    }
  }

}
