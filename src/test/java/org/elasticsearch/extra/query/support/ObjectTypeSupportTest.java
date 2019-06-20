package org.elasticsearch.extra.query.support;

import org.elasticsearch.extra.context.internal.ObjectResolver;
import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.query.BoolQueryAttributeContextImpl;
import org.elasticsearch.extra.query.annotation.ObjectType;
import org.elasticsearch.extra.query.annotation.QueryType;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ObjectTypeSupportTest {
  @Test
  public void get() {
    ObjectTypeSupport support = new ObjectTypeSupport();
    BoolQueryBuilder builder = new BoolQueryBuilder();
    ObjectModel model = new ObjectModel(new Inner("a", "b"));
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(ObjectModel.class);
    for (Property property : properties) {
      support.initialize(property, new BoolQueryAttributeContextImpl());
      BoolQueryHandler handler = support.get().get();
      boolean flag = handler.test(builder, model);
      Assert.assertTrue(flag);
      List<QueryBuilder> must = builder.must();
      Assert.assertEquals(2, must.size());
      BoolQueryBuilder inner = new BoolQueryBuilder();
      inner.must(QueryBuilders.termQuery("inner.a", "a"));
      inner.must(QueryBuilders.termQuery("inner.b", "b"));
      Assert.assertEquals(inner, builder);
    }
  }

  @Test
  public void forNull() {
    ObjectTypeSupport support = new ObjectTypeSupport();
    BoolQueryBuilder builder = new BoolQueryBuilder();
    ObjectModel model = new ObjectModel(null);
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(ObjectModel.class);
    for (Property property : properties) {
      support.initialize(property, new BoolQueryAttributeContextImpl());
      BoolQueryHandler handler = support.get().get();
      boolean flag = handler.test(builder, model);
      Assert.assertFalse(flag);
    }
  }

  public static class ObjectModel {
    @ObjectType(field = "inner")
    private Inner inner;

    public ObjectModel(Inner inner) {
      this.inner = inner;
    }

    public Inner getInner() {
      return inner;
    }
  }

  public static class Inner {
    @QueryType(field = "a")
    private String a;
    @QueryType(field = "b")
    private String b;

    public Inner(String a, String b) {
      this.a = a;
      this.b = b;
    }

    public String getA() {
      return a;
    }

    public String getB() {
      return b;
    }
  }
}