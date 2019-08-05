package org.elasticsearch.extra.indices;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.extra.context.annotation.Document;
import org.elasticsearch.extra.context.annotation.DocumentValue;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CreateIndexRequestFactory {
  private Consumer<Settings.Builder> analysis;

  public void setAnalysis(String source, XContentType contentType) {
    Objects.requireNonNull(source);
    Objects.requireNonNull(contentType);
    this.analysis = settings -> settings.loadFromSource(source, contentType);
  }

  public CreateIndexRequest create(Class<?> type, Supplier<XContentBuilder> supplier) {
    return create(type, supplier.get());
  }

  public CreateIndexRequest create(Class<?> type, XContentBuilder builder) {
    Document document = Objects.requireNonNull(type, "type must be not null").getAnnotation(Document.class);
    BiConsumer<CreateIndexRequest, String> consumer = (request, indexType) -> request.mapping(indexType, builder);
    return create(document, consumer);
  }

  public CreateIndexRequest create(Class<?> type, String source, XContentType contentType) {
    Document document = Objects.requireNonNull(type, "type must be not null").getAnnotation(Document.class);
    BiConsumer<CreateIndexRequest, String> consumer = (request, indexType) -> request.mapping(indexType, source, contentType);
    return create(document, consumer);
  }

  private CreateIndexRequest create(Document document, BiConsumer<CreateIndexRequest, String> consumer) {
    DocumentValue documentValue = DocumentValue.create(document)
            .orElseThrow(() -> new IllegalArgumentException("@Document must be not null"));
    CreateIndexRequest request = new CreateIndexRequest();
    request.index(documentValue.index());
    Settings.Builder settings = Settings.builder();
    if (Objects.nonNull(analysis)) {
      analysis.accept(settings);
    }
    settings.put("index.number_of_shards", document.shards());
    settings.put("index.number_of_replicas", document.replicas());
    request.settings(settings);
    consumer.accept(request, document.type());
    return request;
  }

}
