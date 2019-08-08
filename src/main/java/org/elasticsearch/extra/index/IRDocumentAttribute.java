package org.elasticsearch.extra.index;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.extra.context.annotation.Document;

public class IRDocumentAttribute implements IndexRequestAttribute {
  private Document document;

  @Override
  public void initialize(Class<?> type) {
    document = type.getAnnotation(Document.class);
  }

  @Override
  public <T> void accept(IndexRequest request, T entity) {
    if (document == null) {
      return;
    }
    request.index(document.index());
    request.type(document.type());
  }
}