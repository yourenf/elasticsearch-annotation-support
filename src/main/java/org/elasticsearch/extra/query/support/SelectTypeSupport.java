package org.elasticsearch.extra.query.support;

import org.elasticsearch.extra.NotSupportedException;
import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.context.internal.ReflectUtil;
import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.extra.query.BoolQueryAttributeContext;
import org.elasticsearch.extra.query.annotation.SelectType;
import org.elasticsearch.extra.query.bool.BoolQueryAttribute;
import org.elasticsearch.extra.query.plugin.strategy.Strategy;
import org.elasticsearch.extra.query.plugin.converter.Converter;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;
import org.elasticsearch.extra.query.support.handler.SelectQueryHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SelectTypeSupport implements AnnotationSupport {
  private Property property;
  private String path;
  private List<SelectType> types;
  private Converter<?, ?> converter;

  @Override
  public void initialize(Property property, BoolQueryAttributeContext context) {
    this.property = property;
    this.converter = context.getFactory().findConverter(property.getType());
    SelectType type = property.getAnnotation(SelectType.class);
    SelectType.List list = property.getAnnotation(SelectType.List.class);
    if (type == null && list == null) {
      this.types = Collections.emptyList();
    } else if (type != null) {
      this.types = Arrays.asList(type);
    } else {
      this.types = Arrays.asList(list.value());
    }
  }

  @Override
  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public Optional<BoolQueryHandler> get() {
    if (types.isEmpty()) {
      return Optional.empty();
    }
    List<SelectQueryHandler.StringQueryHandler> list = getStrategyHandlers();
    SelectQueryHandler handler = new SelectQueryHandler();
    handler.setStrategyHandlers(list);
    handler.setGetter(property.getReadMethod());
    return Optional.of(handler);
  }


  private List<SelectQueryHandler.StringQueryHandler> getStrategyHandlers() {
    return types.stream().map(type -> {
      String rule = type.rule();
      Strategy strategy = ReflectUtil.create(type.strategy());
      strategy.setRule(rule);
      SelectQueryHandler.StringQueryHandler handler = new SelectQueryHandler.StringQueryHandler();
      if (!handler.support(property.getType())) {
        throw new NotSupportedException("注解@SelectType不支持类型" + property.getType());
      }
      handler.setConverter(converter);
      handler.setStrategy(strategy);
      handler.setAttribute(createAttribute(type));
      handler.setContext(createContext(type));
      return handler;
    }).collect(Collectors.toList());
  }

  private BoolQueryAttribute<?> createAttribute(SelectType type) {
    Class<? extends BoolQueryAttribute<?>> query = type.attribute();
    return ReflectUtil.create(query);
  }

  private AttributeContext createContext(SelectType type) {
    AttributeContext context = new AttributeContext();
    context.field(type.field());
    context.path(path);
    context.boost(type.boost());
    return context;
  }
}
