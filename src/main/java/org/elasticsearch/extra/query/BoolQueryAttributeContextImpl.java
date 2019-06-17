package org.elasticsearch.extra.query;

import org.elasticsearch.extra.context.internal.ObjectResolver;
import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.context.internal.ReflectUtil;
import org.elasticsearch.extra.query.plugin.converter.BeanConverterFactory;
import org.elasticsearch.extra.query.support.AnnotationSupport;
import org.elasticsearch.extra.query.support.NestedTypeSupport;
import org.elasticsearch.extra.query.support.QueryTypeSupport;
import org.elasticsearch.extra.query.support.SelectTypeSupport;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BoolQueryAttributeContextImpl implements BoolQueryAttributeContext {
  private List<Class<? extends AnnotationSupport>> supportTypes = new ArrayList<>();
  private BeanConverterFactory factory;

  @Override
  public void add(Class<? extends AnnotationSupport> supportType) {
    Objects.requireNonNull(supportTypes);
    supportTypes.add(supportType);
  }

  @Override
  public <T> BoolQueryConverter<T> create(Class<T> type, String path) {
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(type);
    List<BoolQueryHandler> collect = properties.stream()
            .map(property -> createByProperty(property, path))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    return new BoolQueryConverter<>(collect);
  }

  private BoolQueryHandler createByProperty(Property property, String path) {
    List<Class<? extends AnnotationSupport>> list = new ArrayList<>(supportTypes);
    list.add(NestedTypeSupport.class);
    list.add(QueryTypeSupport.class);
    list.add(SelectTypeSupport.class);
    List<BoolQueryHandler> handlers = list.stream().map(ReflectUtil::create).map(s -> {
      s.initialize(property, this);
      s.setPath(path);
      return s.get();
    }).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    if (handlers.size() >= 1) {
      return handlers.get(0);
    } else {
      return null;
    }
  }

  @Override
  public BeanConverterFactory getBeanConverterFactory() {
    if (Objects.isNull(factory)) {
      factory = new BeanConverterFactory();
    }
    return factory;
  }

  @Override
  public void setBeanConverterFactory(BeanConverterFactory factory) {
    this.factory = Objects.requireNonNull(factory);
  }

  @Override
  public void setBeanConverterFactory(Supplier<BeanConverterFactory> supplier) {
    Objects.requireNonNull(supplier);
    this.factory = Objects.requireNonNull(supplier.get());
  }
}
