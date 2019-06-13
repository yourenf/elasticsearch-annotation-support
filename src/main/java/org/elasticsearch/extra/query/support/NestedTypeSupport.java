package org.elasticsearch.extra.query.support;

import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.query.BoolQueryAttributeContext;
import org.elasticsearch.extra.query.BoolQueryConverter;
import org.elasticsearch.extra.query.annotation.NestedType;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;
import org.elasticsearch.extra.query.support.handler.NestedQueryHandler;

import java.util.Optional;

public class NestedTypeSupport implements AnnotationSupport {
  private BoolQueryAttributeContext context;
  private Property property;
  private NestedType nestedType;

  @Override
  public void initialize(Property property, BoolQueryAttributeContext context) {
    this.property = property;
    this.context = context;
    nestedType = property.getAnnotation(NestedType.class);
  }


  @Override
  public Optional<BoolQueryHandler> get() {
    if (nestedType == null) {
      return Optional.empty();
    }
    BoolQueryConverter<?> converter = context.create(property.getType(), nestedType.path());
    NestedQueryHandler handler = new NestedQueryHandler();
    handler.setNestedType(nestedType);
    handler.setAttribute(converter);
    handler.setGetter(property.getReadMethod());
    return Optional.of(handler);
  }
}
