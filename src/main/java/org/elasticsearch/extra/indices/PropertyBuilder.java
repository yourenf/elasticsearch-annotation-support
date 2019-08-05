package org.elasticsearch.extra.indices;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.extra.JsonPropertiesException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class PropertyBuilder {
  private List<Consumer<XContentBuilder>> properties;

  public PropertyBuilder() {
    this.properties = new ArrayList<>();
  }

  public XContentBuilder build() {
    if (properties.isEmpty()) {
      throw new IllegalArgumentException("properties未设置");
    }
    try {
      XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
      jsonBuilder.startObject();
      jsonBuilder.startObject("properties");
      for (Consumer<XContentBuilder> property : properties) {
        property.accept(jsonBuilder);
      }
      jsonBuilder.endObject();
      jsonBuilder.endObject();
      return jsonBuilder;
    } catch (IOException e) {
      throw new JsonPropertiesException("创建JSON失败", e);
    }
  }

  public PropertyBuilder fieldKeyword(String name) {
    return field(name, "keyword");
  }

  public PropertyBuilder fieldInt(String name) {
    return field(name, "integer");
  }

  public PropertyBuilder fieldLong(String name) {
    return field(name, "long");
  }

  public PropertyBuilder field(String name, String type) {
    return field(name, type, null);
  }

  public PropertyBuilder field(String name, String type, Consumer<XContentBuilder> consumer) {
    properties.add((builder) -> {
      try {
        builder.startObject(name);
        builder.field("type", type);
        if (Objects.nonNull(consumer)) {
          consumer.accept(builder);
        }
        builder.endObject();
      } catch (IOException e) {
        throw new JsonPropertiesException("创建JSON失败 name {} ,type {}", name, type, e);
      }
    });
    return this;
  }

  public PropertyBuilder field(Consumer<XContentBuilder> consumer) {
    Objects.requireNonNull(consumer);
    properties.add(consumer);
    return this;
  }
}
