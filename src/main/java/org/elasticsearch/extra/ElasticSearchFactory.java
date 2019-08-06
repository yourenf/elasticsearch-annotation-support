package org.elasticsearch.extra;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.extra.index.IndexRequestConfig;
import org.elasticsearch.extra.index.IndexRequestFactory;
import org.elasticsearch.extra.query.SearchRequestConfig;
import org.elasticsearch.extra.query.SearchRequestFactory;
import org.elasticsearch.extra.query.attribute.SearchSourceBuilderAttribute;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ElasticSearchFactory {

  private Map<Class<?>, SearchRequestFactory<?>> searchRequestProviders = new ConcurrentHashMap<>();
  private ConcurrentMap<Class<?>, IndexRequestFactory<?>> indexRequestProviders = new ConcurrentHashMap<>();

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
    SearchRequestFactory<T> provider = (SearchRequestFactory<T>) searchRequestProviders.computeIfAbsent(type,
            k -> searchRequestConfig.create(k));
    return provider.create(entity, onceAttribute);
  }

  public <T> IndexRequest createIndexRequest(T entity) {
    return createIndexRequest(entity, null);
  }

  /**
   * @param entity 文档
   * @param id     文档ID
   * @param <T>    文档类型
   * @return IndexRequest
   */
  public <T> IndexRequest createIndexRequest(T entity, String id) {
    Objects.requireNonNull(entity, "entity 参数不能为空");
    Class<?> type = entity.getClass();
    IndexRequestFactory<T> provider = (IndexRequestFactory<T>) indexRequestProviders.computeIfAbsent(type,
            k -> indexRequestConfig.create(k));
    return provider.create(entity, id);
  }

}
