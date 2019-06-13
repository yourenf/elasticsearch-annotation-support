package org.elasticsearch.extra.index;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.extra.context.annotation.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class IRDocumentAttribute implements IndexRequestAttribute {
  private static final Logger log = LoggerFactory.getLogger(IRDocumentAttribute.class);
  private Class<?> type;
  private Document document;

  @Override
  public void initialize(Class<?> type) {
    this.type = Objects.requireNonNull(type, "type must be not null");
    document = type.getAnnotation(Document.class);
  }

  @Override
  public <T> void accept(IndexRequest request, T entity) {
    if (document == null) {
      log.warn("can not found annotation @Document at {}", type);
      return;
    }
    request.index(document.index());
    request.type(document.type());
  }
}