package org.elasticsearch.extra.indices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.extra.context.annotation.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class CreateIndexRequestFactoryTest {

  private Path getResource(String filename) throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    URL url = classLoader.getResource("");
    Path resource = Paths.get(url.toURI());
    return Paths.get(resource.toString(), filename);
  }

  @Test
  public void accept() throws IOException, URISyntaxException {
    CreateIndexRequestFactory factory = new CreateIndexRequestFactory();
    byte[] bytes = Files.readAllBytes(getResource("analysis.json"));
    factory.setAnalysis(new String(bytes, "utf-8"), XContentType.JSON);
    byte[] bytes2 = Files.readAllBytes(getResource("mapping.json"));
    CreateIndexRequest request = factory.create(CIRModel.class, new String(bytes2, "utf-8"), XContentType.JSON);

    CreateIndexRequest request2 = factory.create(CIRModel.class, () -> {
      PropertyBuilder builder = new PropertyBuilder();
      builder.fieldKeyword("name");
      builder.fieldLong("value");
      return builder.build();
    });
    Assert.assertEquals(print(request), print(request2));
  }

  private String print(CreateIndexRequest request) throws IOException {
    XContentBuilder content = request.toXContent(XContentFactory.jsonBuilder(), ToXContent.EMPTY_PARAMS);
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    JsonNode tree = mapper.readTree(Strings.toString(content));
    return mapper.writeValueAsString(tree);
  }

  @Document(index = "a")
  private static class CIRModel {
    private String name;
    private Integer vale;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getVale() {
      return vale;
    }

    public void setVale(Integer vale) {
      this.vale = vale;
    }
  }
}