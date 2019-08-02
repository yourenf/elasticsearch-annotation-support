package org.elasticsearch.extra.context.annotation;

import org.elasticsearch.extra.context.internal.ReflectUtil;

import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class DocumentValue {
  private Document document;

  private DocumentValue(Document document) {
    this.document = Objects.requireNonNull(document);
  }

  public static Optional<DocumentValue> create(Document document) {
    return Optional.ofNullable(document).map(DocumentValue::new);
  }

  public String index() {
    if (document.indexOperator() == NoneOperator.class) {
      return document.index();
    }
    Class<? extends UnaryOperator<String>> operatorType = document.indexOperator();
    UnaryOperator<String> operator = ReflectUtil.create(operatorType);
    return operator.apply(document.index());
  }

  public String type() {
    return document.type();
  }
}
