package org.elasticsearch.extra.query.attribute;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.extra.context.annotation.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class SRDocumentAttribute implements SearchRequestAttribute {
  private static final Logger log = LoggerFactory.getLogger(SRDocumentAttribute.class);
  private Class<?> type;
  private Document document;

  @Override
  public void initialize(Class<?> type) {
    this.type = Objects.requireNonNull(type, "type must be not null");
    document = type.getAnnotation(Document.class);
  }

  @Override
  public void accept(SearchRequest request) {
    if (document == null) {
      log.warn("can not found annotation @Document at {}", type);
      return;
    }
    request.indices(document.index());
    request.types(document.type());
  }
}
