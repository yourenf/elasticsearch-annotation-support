package org.elasticsearch.extra.index;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.extra.context.annotation.Routing;
import org.junit.Assert;
import org.junit.Test;

public class IRRoutingAttributeTest {

  @Test
  public void accept() {
    IRRoutingAttribute attribute = new IRRoutingAttribute();

    attribute.initialize(RoutingModel.class);

    IndexRequest request = new IndexRequest();
    request.index("index");
    request.type("type");
    request.id("id");
    RoutingModel model = new RoutingModel("a", "b");
    attribute.accept(request, model);
    String routing = request.routing();
    Assert.assertEquals("a", routing);
  }

  public static final class RoutingModel {
    @Routing
    private String value;
    private String value2;

    public RoutingModel(String value, String value2) {
      this.value = value;
      this.value2 = value2;
    }

    public String getValue() {
      return value;
    }

    public String getValue2() {
      return value2;
    }
  }
}