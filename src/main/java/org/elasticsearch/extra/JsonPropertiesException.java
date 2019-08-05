package org.elasticsearch.extra;

public class JsonPropertiesException extends RuntimeException {

  public JsonPropertiesException(String msg) {
    super(msg);
  }

  public JsonPropertiesException(Exception e) {
    super(e);
  }

  public JsonPropertiesException(String msg, Exception e) {
    super(msg, e);
  }

  public JsonPropertiesException(String format, Object... values) {
    super(String.format(format, values));
  }
}
