package org.elasticsearch.extra;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.extra.index.IndexRequestAttribute;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class IndexRequestProvider<T> {

  private Function<Object, String> serializer;

  private final List<IndexRequestAttribute> attributes;

  public IndexRequestProvider(Function<Object, String> serializer, List<IndexRequestAttribute> attributes) {
    this.serializer = Objects.requireNonNull(serializer);
    this.attributes = attributes;
  }

  public IndexRequest create(T entity, String id) {
    Objects.requireNonNull(entity, "查询参数不能为空");
    Objects.requireNonNull(id, "id不能为空");
    IndexRequest request = new IndexRequest();
    for (IndexRequestAttribute attribute : attributes) {
      attribute.accept(request, entity);
    }
    request.id(id);
    String json = serializer.apply(entity);
    request.source(json, XContentType.JSON);
    return request;
  }
}
