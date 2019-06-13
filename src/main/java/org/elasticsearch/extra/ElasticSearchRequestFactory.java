package org.elasticsearch.extra;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.extra.query.attribute.SearchSourceBuilderAttribute;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ElasticSearchRequestFactory {

  private Map<Class<?>, SearchRequestProvider<?>> searchRequestProviders = new ConcurrentHashMap<>();
  private ConcurrentMap<Class<?>, IndexRequestProvider<?>> indexRequestProviders = new ConcurrentHashMap<>();

  private IndexRequestConfig indexRequestConfig;
  private SearchRequestConfig searchRequestConfig;

  public void setIndexRequestConfig(IndexRequestConfig indexRequestConfig) {
    this.indexRequestConfig = indexRequestConfig;
  }

  public void setSearchRequestConfig(SearchRequestConfig searchRequestConfig) {
    this.searchRequestConfig = searchRequestConfig;
  }

  public <T> SearchRequest createSearchRequest(T entity, SearchSourceBuilderAttribute onceAttribute) {
    Objects.requireNonNull(entity, "查询参数不能为空");
    Class<?> type = entity.getClass();
    SearchRequestProvider<T> provider = (SearchRequestProvider<T>) searchRequestProviders.computeIfAbsent(type,
            k -> searchRequestConfig.create(k));
    return provider.create(entity, onceAttribute);
  }

  public <T> IndexRequest createIndexRequest(T entity, String id) {
    Objects.requireNonNull(entity, "查询参数不能为空");
    Objects.requireNonNull(id, "id不能为空");
    Class<?> type = entity.getClass();
    IndexRequestProvider<T> provider = (IndexRequestProvider<T>) indexRequestProviders.computeIfAbsent(type,
            k -> indexRequestConfig.createProvider(k));
    return provider.create(entity, id);
  }

}
