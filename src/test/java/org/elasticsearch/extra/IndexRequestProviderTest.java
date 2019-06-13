package org.elasticsearch.extra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.extra.context.annotation.Document;
import org.elasticsearch.extra.context.annotation.Routing;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class IndexRequestProviderTest {

  @Test
  public void create() throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    IndexRequestConfig config = new IndexRequestConfig(value -> {
      try {
        return mapper.writeValueAsString(value);
      } catch (JsonProcessingException e) {
        return "";
      }
    });
    IndexRequestProvider<PModel> provider = config.createProvider(PModel.class);

    IndexRequest request = provider.create(new PModel("1", 2), "ss");
    BytesReference source = request.source();
    Map<String, Object> expect = new LinkedHashMap<>();
    expect.put("value", "1");
    expect.put("value2", 2);
    IModel m = new IModel("1", 2);
    expect.put("model", m);
    expect.put("modelArray", Arrays.asList(m, m, m));
    Assert.assertEquals(mapper.writeValueAsString(expect), source.utf8ToString());
  }

  public static final class PModel {
    private String value;
    private Integer value2;
    private IModel model;
    private List<IModel> modelArray;

    public PModel(String value, Integer value2) {
      this.value = value;
      this.value2 = value2;
      this.model = new IModel(value, value2);
      this.modelArray = Arrays.asList(model, model, model);
    }

    public String getValue() {
      return value;
    }

    public Integer getValue2() {
      return value2;
    }

    public IModel getModel() {
      return model;
    }

    public List<IModel> getModelArray() {
      return modelArray;
    }
  }

  public static final class IModel {
    private String value;
    private Integer value2;

    public IModel(String value, Integer value2) {
      this.value = value;
      this.value2 = value2;
    }

    public String getValue() {
      return value;
    }

    public Integer getValue2() {
      return value2;
    }
  }
}