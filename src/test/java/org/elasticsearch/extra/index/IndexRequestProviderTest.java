package org.elasticsearch.extra.index;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.extra.index.IndexRequestConfig;
import org.elasticsearch.extra.index.IndexRequestProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IndexRequestProviderTest {

  @Test
  public void create() throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    IndexRequestConfig config = new IndexRequestConfig(value -> {
      try {
        return mapper.writeValueAsString(value);
      } catch (JsonProcessingException e) {
        return "";
      }
    });
    IndexRequestProvider<PModel> provider = config.createProvider(PModel.class);
    PModel model = new PModel("1", 2);
    IndexRequest request = provider.create(model, "ss");
    BytesReference source = request.source();
    PModel value = mapper.readValue(source.utf8ToString(), PModel.class);
    Assert.assertTrue(model.equals(value));
  }

  public static final class PModel {
    private String value;
    private Integer value2;
    private IModel model;
    private List<IModel> modelArray;

    public PModel() {
    }

    public PModel(String value, Integer value2) {
      this.value = value;
      this.value2 = value2;
      this.model = new IModel(value, value2);
      this.modelArray = Arrays.asList(model, model, model);
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public Integer getValue2() {
      return value2;
    }

    public void setValue2(Integer value2) {
      this.value2 = value2;
    }

    public IModel getModel() {
      return model;
    }

    public void setModel(IModel model) {
      this.model = model;
    }

    public List<IModel> getModelArray() {
      return modelArray;
    }

    public void setModelArray(List<IModel> modelArray) {
      this.modelArray = modelArray;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PModel pModel = (PModel) o;
      return Objects.equals(value, pModel.value) &&
              Objects.equals(value2, pModel.value2) &&
              Objects.equals(model, pModel.model) &&
              Objects.equals(modelArray, pModel.modelArray);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, value2, model, modelArray);
    }

    @Override
    public String toString() {
      return "PModel{" +
              "value='" + value + '\'' +
              ", value2=" + value2 +
              ", model=" + model +
              ", modelArray=" + modelArray +
              '}';
    }
  }

  public static final class IModel {
    private String value;
    private Integer value2;

    public IModel() {
    }

    public IModel(String value, Integer value2) {
      this.value = value;
      this.value2 = value2;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public Integer getValue2() {
      return value2;
    }

    public void setValue2(Integer value2) {
      this.value2 = value2;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      IModel iModel = (IModel) o;
      return Objects.equals(value, iModel.value) &&
              Objects.equals(value2, iModel.value2);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, value2);
    }

    @Override
    public String toString() {
      return "IModel{" +
              "value='" + value + '\'' +
              ", value2=" + value2 +
              '}';
    }
  }
}