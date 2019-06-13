package org.elasticsearch.extra.query;

import java.util.*;

public class AttributeContext {
  /**
   * @see org.elasticsearch.extra.query.annotation.NestedType#path
   */
  private String path;
  /**
   * @see org.elasticsearch.extra.query.annotation.QueryType#field
   * @see org.elasticsearch.extra.query.annotation.SelectType#field
   */
  private String field;
  /**
   * @see org.elasticsearch.extra.query.annotation.QueryType#boost
   * @see org.elasticsearch.extra.query.annotation.SelectType#boost
   */
  private float boost;
  private Map<String, Object> parameters;

  public void path(String path) {
    this.path = path;
  }

  public Optional<String> path() {
    return Optional.ofNullable(path);
  }

  public String field() {
    return field;
  }

  public void field(String field) {
    this.field = field;
  }

  /**
   * path + field
   *
   * @return 自动
   */
  public String completeField() {
    if (Objects.isNull(path) || path.isEmpty()) {
      return field;
    } else {
      return path + "." + field;
    }
  }

  public float boost() {
    return boost;
  }


  public void boost(float boost) {
    this.boost = boost;
  }

  /**
   * 额外的参数 自定义注解传参
   *
   * @return
   */
  public Map<String, Object> parameters() {
    if (parameters == null) {
      parameters = new HashMap<>();
    }
    return parameters;
  }
}
