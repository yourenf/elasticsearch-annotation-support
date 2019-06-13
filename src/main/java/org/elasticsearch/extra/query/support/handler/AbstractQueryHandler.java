package org.elasticsearch.extra.query.support.handler;

import org.elasticsearch.extra.query.plugin.converter.Converter;

public abstract class AbstractQueryHandler implements BoolQueryHandler {
  protected Converter converter;

  public void setConverter(Converter<?, ?> converter) {
    this.converter = converter;
  }

  abstract boolean support(Class<?> handlerType);
}
