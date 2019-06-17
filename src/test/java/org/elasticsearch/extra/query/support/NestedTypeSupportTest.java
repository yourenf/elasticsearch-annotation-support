package org.elasticsearch.extra.query.support;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.extra.context.internal.ObjectResolver;
import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.query.BoolQueryAttributeContextImpl;
import org.elasticsearch.extra.query.annotation.NestedType;
import org.elasticsearch.extra.query.annotation.QueryType;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class NestedTypeSupportTest {

  @Test
  public void get() {
    NestedTypeSupport support = new NestedTypeSupport();
    BoolQueryBuilder builder = new BoolQueryBuilder();
    NestedModel model = new NestedModel(new Inner("a", "b"));
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(NestedModel.class);
    for (Property property : properties) {
      support.initialize(property, new BoolQueryAttributeContextImpl());
      BoolQueryHandler handler = support.get().get();
      boolean flag = handler.test(builder, model);
      Assert.assertTrue(flag);
      List<QueryBuilder> must = builder.must();
      Assert.assertEquals(1, must.size());
      BoolQueryBuilder inner = new BoolQueryBuilder();
      inner.must(QueryBuilders.termQuery("inner.a", "a"));
      inner.must(QueryBuilders.termQuery("inner.b", "b"));
      NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder("inner", inner, ScoreMode.None);
      Assert.assertEquals(nestedQueryBuilder, must.get(0));
    }
  }

  @Test
  public void forNull() {
    NestedTypeSupport support = new NestedTypeSupport();
    BoolQueryBuilder builder = new BoolQueryBuilder();
    NestedModel model = new NestedModel(null);
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(NestedModel.class);
    for (Property property : properties) {
      support.initialize(property, new BoolQueryAttributeContextImpl());
      BoolQueryHandler handler = support.get().get();
      boolean flag = handler.test(builder, model);
      Assert.assertFalse(flag);
    }
  }

  public static class NestedModel {
    @NestedType(path = "inner")
    private Inner inner;

    public NestedModel(Inner inner) {
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