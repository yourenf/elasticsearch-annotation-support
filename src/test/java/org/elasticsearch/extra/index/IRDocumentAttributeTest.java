package org.elasticsearch.extra.index;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.extra.context.annotation.Document;
import org.elasticsearch.extra.context.annotation.Routing;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class IRDocumentAttributeTest {
  @Test
  public void accept() {
    IRDocumentAttribute attribute = new IRDocumentAttribute();

    attribute.initialize(DocModel.class);

    IndexRequest request = new IndexRequest();
    attribute.accept(request, new DocModel());
    Assert.assertEquals("index", request.index());
    Assert.assertEquals("_doc", request.type());
  }

  @Document(index = "index")
  public static final class DocModel {

  }
}