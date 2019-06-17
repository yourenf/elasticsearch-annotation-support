package org.elasticsearch.extra;

import org.elasticsearch.extra.context.internal.ReflectUtil;
import org.elasticsearch.extra.query.*;
import org.elasticsearch.extra.query.attribute.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SearchRequestConfig {

  private BoolQueryAttributeContext boolQueryAttributeContext;
  private List<Class<? extends SearchRequestAttribute>> searchRequestAttributes;
  private List<Class<? extends SearchSourceBuilderAttribute>> searchSourceBuilderAttributes;

  public SearchRequestConfig() {
    searchRequestAttributes = new ArrayList<>();
    searchSourceBuilderAttributes = new ArrayList<>();

    searchRequestAttributes.add(SRDocumentAttribute.class);
    searchSourceBuilderAttributes.add(HighlightAttribute.class);
    searchSourceBuilderAttributes.add(CollapseBuilderAttribute.class);
  }

  public void setBoolQueryAttributeContext(BoolQueryAttributeContext context) {
    this.boolQueryAttributeContext = context;
  }

  public void setBoolQueryAttributeContextSupplier(Supplier<BoolQueryAttributeContext> context) {
    this.boolQueryAttributeContext = context.get();
  }

  public void addSearchRequestAttribute(Class<? extends SearchRequestAttribute> attribute) {
    searchRequestAttributes.add(attribute);
  }

  public void addSearchSourceBuilderAttribute(Class<? extends SearchSourceBuilderAttribute> attribute) {
    searchSourceBuilderAttributes.add(attribute);
  }

  public <T> SearchRequestProvider<T> create(Class<T> type) {
    if (Objects.isNull(boolQueryAttributeContext)) {
      boolQueryAttributeContext = new BoolQueryAttributeContextImpl();
    }
    List<SearchRequestAttribute> collect = searchRequestAttributes.stream()
            .map(ReflectUtil::create)
            .peek(a -> a.initialize(type))
            .collect(Collectors.toList());

    List<SearchSourceBuilderAttribute> attributes = searchSourceBuilderAttributes.stream()
            .map(ReflectUtil::create)
            .peek(a -> a.initialize(type))
            .collect(Collectors.toList());

    BoolQueryConverter<T> converter = boolQueryAttributeContext.create(type, "");
    return new SearchRequestProvider(converter, collect, attributes);

  }
}