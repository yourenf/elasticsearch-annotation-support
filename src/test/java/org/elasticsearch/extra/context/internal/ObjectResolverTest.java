package org.elasticsearch.extra.context.internal;

import org.elasticsearch.extra.query.annotation.QueryType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ObjectResolverTest {

  @Test
  public void getProperties() {
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(A.class);
    for (Property property : properties) {
      QueryType type = property.getAnnotation(QueryType.class);
      Assert.assertNotNull(type);
    }
  }

  public static class A {
    @QueryType(field = "fff")
    private String value;
    private String value2;

    public A(String value, String value2) {
      this.value = value;
      this.value2 = value2;
    }

    public String getValue() {
      return value;
    }

    @QueryType(field = "fff")
    public String getValue2() {
      return value2;
    }
  }

}