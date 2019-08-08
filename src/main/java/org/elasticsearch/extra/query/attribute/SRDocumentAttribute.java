package org.elasticsearch.extra.query.attribute;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.extra.context.annotation.Document;

public class SRDocumentAttribute implements SearchRequestAttribute {
  private Document document;

  @Override
  public void initialize(Class<?> type) {
    document = type.getAnnotation(Document.class);
  }

  @Override
  public void accept(SearchRequest request) {
    if (document == null) {
      return;
    }
    request.indices(document.index());
    request.types(document.type());
  }
}
