package org.elasticsearch.extra;

public class ElasticSearchException extends RuntimeException {

  public ElasticSearchException(String msg) {
    super(msg);
  }

  public ElasticSearchException(Exception e) {
    super(e);
  }

  public ElasticSearchException(String msg, Exception e) {
    super(msg, e);
  }

  public ElasticSearchException(String format, Object... values) {
    super(String.format(format, values));
  }
}
