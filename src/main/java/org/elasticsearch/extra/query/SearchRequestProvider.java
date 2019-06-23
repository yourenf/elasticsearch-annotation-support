package org.elasticsearch.extra.query;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.extra.query.attribute.SearchRequestAttribute;
import org.elasticsearch.extra.query.attribute.SearchSourceBuilderAttribute;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class SearchRequestProvider<T> {
  private static final Logger log = LoggerFactory.getLogger(SearchRequestProvider.class);
  private BoolQueryConverter converter;
  private List<SearchRequestAttribute> searchRequestAttributes;
  private List<SearchSourceBuilderAttribute> searchSourceBuilderAttributes;

  public SearchRequestProvider(BoolQueryConverter converter,
                               List<SearchRequestAttribute> searchRequestAttributes,
                               List<SearchSourceBuilderAttribute> searchSourceBuilderAttributes) {
    this.converter = converter;
    this.searchRequestAttributes = searchRequestAttributes;
    this.searchSourceBuilderAttributes = searchSourceBuilderAttributes;
  }

  public SearchRequest create(T entity, SearchSourceBuilderAttribute... onceAttributes) {
    Objects.requireNonNull(entity, "查询参数不能为空");
    SearchRequest request = new SearchRequest();
    for (SearchRequestAttribute searchRequestAttribute : searchRequestAttributes) {
      searchRequestAttribute.accept(request);
    }
    SearchSourceBuilder source = new SearchSourceBuilder();
    for (SearchSourceBuilderAttribute attribute : searchSourceBuilderAttributes) {
      attribute.accept(source);
    }
    if (onceAttributes != null && onceAttributes.length > 0) {
      for (SearchSourceBuilderAttribute onceAttribute : onceAttributes) {
        onceAttribute.initialize(entity.getClass());
        onceAttribute.accept(source);
      }
    }
    BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
    boolean test = converter.test(boolQueryBuilder, entity);
    if (test) {
      source.query(boolQueryBuilder);
      log.debug("query dsl {}", source);
    } else {
      log.debug("empty query condition ");
    }
    request.source(source);
    return request;
  }
}
