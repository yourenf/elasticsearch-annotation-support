package org.elasticsearch.extra.context.annotation;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.UnaryOperator;

import static org.junit.Assert.*;

public class DocumentValueTest {

  @Test
  public void create() {
  }

  @Test
  public void index() {
    Document document = DocValueRaw.class.getAnnotation(Document.class);
    DocumentValue documentValue = DocumentValue.create(document).orElseThrow(IllegalArgumentException::new);
    String index = documentValue.index();
    Assert.assertEquals(document.index(), index);
  }

  @Test
  public void index2() {
    Document document = DocValueEdit.class.getAnnotation(Document.class);
    DocumentValue documentValue = DocumentValue.create(document).orElseThrow(IllegalArgumentException::new);
    String index = documentValue.index();
    Assert.assertEquals(document.index() + document.index() + document.index(), index);
  }

  @Test
  public void type() {
    Document document = DocValueRaw.class.getAnnotation(Document.class);
    DocumentValue documentValue = DocumentValue.create(document).orElseThrow(IllegalArgumentException::new);
    Assert.assertEquals(document.type(), documentValue.type());
  }

  @Document(index = "a")
  public static class DocValueRaw {

  }

  @Document(index = "a", indexOperator = IndexOp.class)
  public static class DocValueEdit {

  }

  public static class IndexOp implements UnaryOperator<String> {

    @Override
    public String apply(String s) {
      return s + s + s;
    }
  }
}