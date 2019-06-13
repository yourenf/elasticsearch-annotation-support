package org.elasticsearch.extra.query.attribute;

import org.elasticsearch.search.builder.SearchSourceBuilder;

public interface SearchSourceBuilderAttribute {

  default void initialize(Class<?> type) {

  }

  void accept(SearchSourceBuilder e);
}
