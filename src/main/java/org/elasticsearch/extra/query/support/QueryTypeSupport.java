package org.elasticsearch.extra.query.support;

import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.context.internal.ReflectUtil;
import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.extra.query.BoolQueryAttributeContext;
import org.elasticsearch.extra.query.annotation.QueryType;
import org.elasticsearch.extra.query.bool.BoolQueryAttribute;
import org.elasticsearch.extra.query.plugin.converter.Converter;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;
import org.elasticsearch.extra.query.support.handler.RawQueryHandler;

import java.util.Optional;

/**
 * QueryType support
 */
public class QueryTypeSupport implements AnnotationSupport {
  private Property property;
  private QueryType queryType;
  private String path;
  private Converter<?, ?> converter;

  @Override
  public void initialize(Property property, BoolQueryAttributeContext context) {
    this.property = property;
    this.queryType = property.getAnnotation(QueryType.class);
    this.converter = context.getFactory().findConverter(property.getType());

  }

  @Override
  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public Optional<BoolQueryHandler> get() {
    if (queryType == null) {
      return Optional.empty();
    }
    RawQueryHandler handler = new RawQueryHandler();
    BoolQueryAttribute<?> attribute = createAttribute();
    handler.setConverter(converter);
    handler.setContext(create());
    handler.setAttribute(attribute);
    handler.setGetter(property.getReadMethod());
    return Optional.of(handler);
  }

  private BoolQueryAttribute<?> createAttribute() {
    Class<? extends BoolQueryAttribute<?>> query = queryType.attribute();
    return ReflectUtil.create(query);
  }

  private AttributeContext create() {
    AttributeContext context = new AttributeContext();
    context.path(path);
    context.field(queryType.field());
    context.boost(queryType.boost());
    return context;
  }
}
