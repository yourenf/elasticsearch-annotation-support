package org.elasticsearch.extra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.extra.query.annotation.NestedType;
import org.elasticsearch.extra.query.annotation.QueryType;
import org.elasticsearch.extra.query.annotation.SelectType;
import org.elasticsearch.extra.query.bool.TermsQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRequestProviderTest {

  @Test
  public void create() throws IOException {
    SearchRequestConfig config = new SearchRequestConfig();
    SearchRequestProvider<QueryModel> provider = config.create(QueryModel.class);
    QueryModel model = new QueryModel("1,2,3", 2);
    SearchRequest request = provider.create(model);
    SearchSourceBuilder source = request.source();
    QueryBuilder query = source.query();
    Map map = new ObjectMapper().readValue(query.toString(), Map.class);
    Map bool = (Map) map.get("bool");
    List must = (List) bool.get("must");
    Map nested = (Map) must.get(0);
    checkNested(nested);
    Map v1 = (Map) must.get(1);
    checkV1(v1);
    Map v2 = (Map) must.get(2);
    checkV2(v2);
    Map v3 = (Map) must.get(3);
    checkV3(v3);
    Map v4 = (Map) must.get(4);
    checkV4(v4);
  }

  private void checkNested(Map nested) {
    Map q = (Map) nested.get("nested");
    Assert.assertEquals("v5", q.get("path"));
    Assert.assertEquals("sum", q.get("score_mode"));
    Map query = (Map) q.get("query");
    Map bool = (Map) query.get("bool");
    List must = (List) bool.get("must");
    Assert.assertEquals(1, must.size());
    Map m = (Map) must.get(0);
    Map terms = (Map) m.get("terms");
    List<Integer> v6 = (List<Integer>) terms.get("v5.v6");
    Assert.assertArrayEquals(new Integer[]{2, 2}, v6.toArray());
  }

  private void checkV1(Map terms) {
    Map values = (Map) terms.get("terms");
    Object v1 = values.get("v1");
    List<String> list = (List<String>) v1;
    Assert.assertArrayEquals(new String[]{"1", "2", "3"}, list.toArray());
  }

  private void checkV2(Map terms) {
    Map values = (Map) terms.get("term");
    Map v2 = (Map) values.get("v2");
    Assert.assertEquals(LocalDate.now().toString(), v2.get("value"));
    Assert.assertEquals(2.0, v2.get("boost"));
  }

  private void checkV3(Map terms) {
    Map values = (Map) terms.get("term");
    Map v3 = (Map) values.get("v3");
    Map v = (Map) v3.get("value");
    Assert.assertEquals("value", v.get("string"));
    Assert.assertEquals("value", v.get("string2"));
  }

  private void checkV4(Map term) {
    Map values = (Map) term.get("term");
    Map v4 = (Map) values.get("v4");
    List v = (List) v4.get("value");
    List<String> list = (List<String>) v;
    Assert.assertArrayEquals(new Integer[]{1, 2, 3}, list.toArray());
  }

  public static final class QueryModel {
    @SelectType(field = "v1")
    @SelectType(field = "v0")
    private String value;
    @QueryType(field = "v4")
    private List<Integer> value4;
    @NestedType(path = "v5", scoreMode = ScoreMode.Total)
    private IModel model;

    public QueryModel(String value, Integer value2) {
      this.value = value;
      this.value4 = Arrays.asList(1, 2, 3);
      this.model = new IModel(value2);
    }

    public String getValue() {
      return value;
    }

    @QueryType(field = "v2", boost = 2.0f)
    public LocalDate getValue2() {
      return LocalDate.now();
    }

    @QueryType(field = "v3")
    public Map<String, String> getValue3() {
      Map<String, String> map = new HashMap<>();
      map.put("string", "value");
      map.put("string2", "value");
      return map;
    }

    public List<Integer> getValue4() {
      return value4;
    }

    public IModel getModel() {
      return model;
    }

  }

  public static final class IModel {
    @QueryType(field = "v6", attribute = TermsQuery.class)
    private List<Integer> value2;

    public IModel(Integer value2) {
      this.value2 = Arrays.asList(value2, value2);
    }

    public List<Integer> getValue2() {
      return value2;
    }
  }
}