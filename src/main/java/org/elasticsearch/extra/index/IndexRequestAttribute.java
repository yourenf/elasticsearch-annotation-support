package org.elasticsearch.extra.index;

import org.elasticsearch.action.index.IndexRequest;

public interface IndexRequestAttribute {

  void initialize(Class<?> type);

  <T> void accept(IndexRequest request, T entity);
}
