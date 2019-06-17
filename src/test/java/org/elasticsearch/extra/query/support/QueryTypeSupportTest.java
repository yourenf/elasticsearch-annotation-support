package org.elasticsearch.extra.query.support;

import org.elasticsearch.extra.context.internal.ObjectResolver;
import org.elasticsearch.extra.context.internal.Property;
import org.elasticsearch.extra.query.AttributeContext;
import org.elasticsearch.extra.query.BoolQueryAttributeContextImpl;
import org.elasticsearch.extra.query.annotation.QueryType;
import org.elasticsearch.extra.query.bool.BoolQueryAttribute;
import org.elasticsearch.extra.query.bool.TermsQuery;
import org.elasticsearch.extra.query.support.handler.BoolQueryHandler;
import org.elasticsearch.index.query.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QueryTypeSupportTest {
  @Test
  public void initialize() {
    QueryTypeSupport support = new QueryTypeSupport();
    BoolQueryAttributeContextImpl context = new BoolQueryAttributeContextImpl();
    BoolQueryBuilder builder = new BoolQueryBuilder();
    Range range = new Range("1", "2");
    String[] values = {"1", "2"};
    ObjectModel model = new ObjectModel(range, values);
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(ObjectModel.class);
    for (Property property : properties) {
      support.initialize(property, context);
      BoolQueryHandler handler = support.get().get();
      boolean flag = handler.test(builder, model);
      Assert.assertTrue(flag);
    }
    List<QueryBuilder> must = builder.must();
    Assert.assertEquals(2, must.size());
    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("valid_data")
            .gte("1").lte("2");
    Assert.assertEquals(rangeQueryBuilder, must.get(0));
    TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("values", values);
    Assert.assertEquals(termsQueryBuilder, must.get(1));
  }

  @Test
  public void forNull() {
    QueryTypeSupport support = new QueryTypeSupport();
    BoolQueryAttributeContextImpl context = new BoolQueryAttributeContextImpl();
    BoolQueryBuilder builder = new BoolQueryBuilder();
    ObjectModel model = new ObjectModel(null, null);
    List<Property> properties = ObjectResolver.INSTANCE.getProperties(ObjectModel.class);
    for (Property property : properties) {
      support.initialize(property, context);
      BoolQueryHandler handler = support.get().get();
      boolean flag = handler.test(builder, model);
      Assert.assertFalse(flag);
    }
  }

  public static class ObjectModel {
    @QueryType(field = "valid_data", attribute = RangeAttribute.class)
    private Range range;
    @QueryType(field = "values", attribute = TermsQuery.class)
    private String[] values;

    public ObjectModel(Range range, String[] values) {
      this.range = range;
      this.values = values;
    }

    public Range getRange() {
      return range;
    }

    public String[] getValues() {
      return values;
    }
  }

  public static class Range {
    private String lte;
    private String gte;

    public Range(String gte, String lte) {
      this.lte = lte;
      this.gte = gte;
    }

    public String getLte() {
      return lte;
    }

    public void setLte(String lte) {
      this.lte = lte;
    }

    public String getGte() {
      return gte;
    }

    public void setGte(String gte) {
      this.gte = gte;
    }
  }

  public static class RangeAttribute implements BoolQueryAttribute<Range> {

    @Override
    public boolean accept(BoolQueryBuilder boolQueryBuilder, AttributeContext context, Range value) {
      RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery(context.field())
              .gte(value.getGte()).lte(value.getLte()).boost(context.boost());
      boolQueryBuilder.must(queryBuilder);
      return true;
    }
  }
}