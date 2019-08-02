package org.elasticsearch.extra.query.attribute;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.extra.context.annotation.Document;
import org.elasticsearch.extra.context.annotation.DocumentValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class SRDocumentAttribute implements SearchRequestAttribute {
  private static final Logger log = LoggerFactory.getLogger(SRDocumentAttribute.class);
  private Class<?> type;
  private Optional<DocumentValue> documentValue;

  @Override
  public void initialize(Class<?> type) {
    this.type = Objects.requireNonNull(type, "type must be not null");
    Document document = type.getAnnotation(Document.class);
    this.documentValue = DocumentValue.create(document);
  }

  @Override
  public void accept(SearchRequest request) {
    if (!documentValue.isPresent()) {
      log.warn("can not found annotation @Document at {}", type);
      return;
    }
    documentValue.ifPresent(value -> {
      request.indices(value.index());
      request.types(value.type());
    });
  }
}
