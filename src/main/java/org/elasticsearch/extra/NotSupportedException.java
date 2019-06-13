package org.elasticsearch.extra;

public class NotSupportedException extends RuntimeException {

  public NotSupportedException(String msg) {
    super(msg);
  }

  public NotSupportedException(Exception e) {
    super(e);
  }

  public NotSupportedException(String msg, Exception e) {
    super(msg, e);
  }

  public NotSupportedException(String format, Object... values) {
    super(String.format(format, values));
  }
}
