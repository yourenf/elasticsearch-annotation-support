package org.elasticsearch.extra.index;

import org.elasticsearch.extra.context.internal.ReflectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IndexRequestConfig {

  private Function<Object, String> serializer;

  private List<Class<? extends IndexRequestAttribute>> attributes;

  public IndexRequestConfig(Function<Object, String> serializer) {
    this.serializer = serializer;
    this.attributes = new ArrayList<>();
    attributes.add(IRDocumentAttribute.class);
    attributes.add(IRRoutingAttribute.class);
  }

  public void addAttribute(Class<? extends IndexRequestAttribute> type) {
    Objects.requireNonNull(type);
    this.attributes.add(type);
  }

  public <T> IndexRequestProvider<T> createProvider(Class<T> type) {
    List<Class<? extends IndexRequestAttribute>> list = new ArrayList<>(attributes);
    List<IndexRequestAttribute> collect = list.stream()
            .map(ReflectUtil::create).collect(Collectors.toList());
    for (IndexRequestAttribute attribute : collect) {
      attribute.initialize(type);
    }
    return new IndexRequestProvider<>(serializer, collect);
  }
}