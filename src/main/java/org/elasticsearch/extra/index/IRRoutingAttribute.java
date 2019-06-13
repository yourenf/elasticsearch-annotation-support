package org.elasticsearch.extra.index;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.extra.NotSupportedException;
import org.elasticsearch.extra.context.annotation.Routing;
import org.elasticsearch.extra.context.internal.ObjectResolver;
import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.context.internal.ReflectUtil;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class IRRoutingAttribute implements IndexRequestAttribute {
  private boolean supported;
  private Method getter;

  @Override
  public void initialize(Class<?> type) {
    Objects.requireNonNull(type, "type must be not null");
    List<Property> list = ObjectResolver.INSTANCE.getProperties(type).stream().filter(p -> {
      Routing routing = p.getAnnotation(Routing.class);
      return Objects.nonNull(routing);
    }).collect(Collectors.toList());
    if (list.isEmpty()) {
      this.supported = false;
    } else if (list.size() == 1) {
      this.supported = true;
      Property property = list.get(0);
      this.getter = property.getReadMethod();
    } else {
      throw new NotSupportedException("不支持两个字段作为routing");
    }
  }

  @Override
  public <T> void accept(IndexRequest request, T entity) {
    if (!supported) {
      return;
    }
    Object value = ReflectUtil.getValue(getter, entity);
    Objects.requireNonNull(value, "routing 不能为空");
    request.routing(value.toString());
  }
}