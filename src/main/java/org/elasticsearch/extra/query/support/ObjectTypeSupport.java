package org.elasticsearch.extra.query.support;

import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.query.BoolQueryAttributeContext;
import org.elasticsearch.extra.query.BoolQueryConverter;
import org.elasticsearch.extra.query.annotation.ObjectType;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;
import org.elasticsearch.extra.query.support.handler.ObjectQueryHandler;

import java.util.Optional;

public class ObjectTypeSupport implements AnnotationSupport {
  private BoolQueryAttributeContext context;
  private Property property;
  private ObjectType objectType;

  @Override
  public void initialize(Property property, BoolQueryAttributeContext context) {
    this.property = property;
    this.context = context;
    this.objectType = property.getAnnotation(ObjectType.class);
  }

  @Override
  public Optional<BoolQueryHandler> get() {
    if (objectType == null) {
      return Optional.empty();
    }
    BoolQueryConverter<?> converter = context.create(property.getType(), objectType.field());
    ObjectQueryHandler handler = new ObjectQueryHandler();
    handler.setAttribute(converter);
    handler.setGetter(property.getReadMethod());
    return Optional.of(handler);
  }
}
