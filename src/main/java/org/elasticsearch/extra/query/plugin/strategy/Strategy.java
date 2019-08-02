package org.elasticsearch.extra.query.plugin.strategy;

public interface Strategy {

  /**
   * 设置策略
   *
   * @param rule
   */
  void setRule(String rule);

  /**
   * 值是否与该策略匹配
   *
   * @param value 检索词
   * @return
   */
  boolean match(String value);

  /**
   * 获取真正的检索词
   *
   * @param value 检索词
   * @return
   */
  String cleanValue(String value);

}
