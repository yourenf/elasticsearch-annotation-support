package org.elasticsearch.extra.index;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.extra.context.annotation.Document;
import org.elasticsearch.extra.context.annotation.DocumentValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class IRDocumentAttribute implements IndexRequestAttribute {
  private static final Logger log = LoggerFactory.getLogger(IRDocumentAttribute.class);
  private Class<?> type;
  private Optional<DocumentValue> documentValue;

  @Override
  public void initialize(Class<?> type) {
    this.type = Objects.requireNonNull(type, "type must be not null");
    Document document = type.getAnnotation(Document.class);
    this.documentValue = DocumentValue.create(document);
  }

  @Override
  public <T> void accept(IndexRequest request, T entity) {
    documentValue.ifPresent(value -> {
      request.index(value.index());
      request.type(value.type());
    });
    if (!documentValue.isPresent()) {
      log.warn("can not found annotation @Document at {}", type);
    }
  }
}