package org.elasticsearch.extra.query.support;

import org.elasticsearch.extra.context.internal.ObjectResolver;
import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.query.BoolQueryAttributeContextImpl;
import org.elasticsearch.extra.query.annotation.QueryType;
import org.elasticsearch.extra.query.annotation.SelectType;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SelectTypeSupportTest {

  @Test
  public void initialize() {
    SelectTypeSupport support = new SelectTypeSupport();
    BoolQueryBuilder builder = new BoolQueryBuilder();
    StringModel model = new StringModel("ar1");
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(StringModel.class);
    for (Property property : properties) {
      support.initialize(property, new BoolQueryAttributeContextImpl());
      BoolQueryHandler handler = support.get().get();
      boolean flag = handler.test(builder, model);
      Assert.assertTrue(flag);
      List<QueryBuilder> must = builder.must();
      Assert.assertEquals(1, must.size());
      TermQueryBuilder expect = QueryBuilders.termQuery("a", "a");
      Assert.assertEquals(expect, must.get(0));
    }
  }

  @Test
  public void setPath() {
  }

  @Test
  public void get() {
  }

  public static class StringModel {
    @SelectType(field = "a", rule = "r1")
    @SelectType(field = "b", rule = "r2")
    private String text;

    public StringModel(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }

  }
}