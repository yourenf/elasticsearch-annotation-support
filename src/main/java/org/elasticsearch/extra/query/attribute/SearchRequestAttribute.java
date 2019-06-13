package org.elasticsearch.extra.query.attribute;

import org.elasticsearch.action.search.SearchRequest;

public interface SearchRequestAttribute {

  void initialize(Class<?> type);

  void accept(SearchRequest searchRequest);
}
