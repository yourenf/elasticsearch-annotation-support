package org.elasticsearch.extra;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 包装 RestHighLevelClient
 */
public final class ElasticSearchClient {

  private static final Logger log = LoggerFactory.getLogger(ElasticSearchClient.class);

  private final RestHighLevelClient client;

  public ElasticSearchClient(String hostname, int port) {
    this(RestClient.builder(new HttpHost(hostname, port)));
  }

  public ElasticSearchClient(HttpHost host, String username, String password) {
    Objects.requireNonNull(username);
    Objects.requireNonNull(password);
    RestClientBuilder builder = RestClient.builder(host)
            .setHttpClientConfigCallback(httpClientBuilder -> {
              httpClientBuilder.disableAuthCaching();
              CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
              UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
              credentialsProvider.setCredentials(AuthScope.ANY, credentials);
              return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
    this.client = new RestHighLevelClient(builder);
  }

  public ElasticSearchClient(String hostname, String port,
                             String username, String password) {
    this(new HttpHost(hostname, Integer.valueOf(port)), username, password);
  }

  public ElasticSearchClient(RestClientBuilder builder) {
    this.client = new RestHighLevelClient(builder);
  }

  public RestHighLevelClient get() {
    return client;
  }

  public boolean ping() {
    try {
      return client.ping(RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("ElasticSearch服务不可用 {}", e.getMessage());
      return false;
    }
  }

  public MainResponse info() {
    try {
      return client.info(RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  public GetResponse get(GetRequest getRequest) {
    try {
      return client.get(getRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  public MultiGetResponse mget(MultiGetRequest request) {
    try {
      return client.mget(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  public SearchResponse search(SearchRequest searchRequest) {
    try {
      return client.search(searchRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  /**
   * 滚动查询
   *
   * @param searchRequest searchRequest
   * @param consumer      消费者
   */
  public void scroll(SearchRequest searchRequest, Consumer<SearchHits> consumer) {
    final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
    searchRequest.scroll(scroll);
    SearchResponse searchResponse = this.search(searchRequest);
    String scrollId = searchResponse.getScrollId();
    try {
      try {
        SearchHits hits = searchResponse.getHits();
        while (hits.getHits().length > 0) {
          consumer.accept(hits);
          SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
          scrollRequest.scroll(scroll);
          searchResponse = client.scroll(scrollRequest, RequestOptions.DEFAULT);
          scrollId = searchResponse.getScrollId();
          hits = searchResponse.getHits();
        }
      } finally {
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        if (!clearScrollResponse.isSucceeded()) {
          client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
        }
      }
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  public MultiSearchResponse msearch(MultiSearchRequest multiSearchRequest) {
    try {
      long begin = System.currentTimeMillis();
      MultiSearchResponse items = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);
      long end = System.currentTimeMillis();
      log.debug("检索 {} 条 花费 {} ms", multiSearchRequest.requests().size(), end - begin);
      return items;
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  @Deprecated
  public MultiSearchResponse multiSearch(MultiSearchRequest multiSearchRequest) {
    try {
      long begin = System.currentTimeMillis();
      MultiSearchResponse items = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);
      long end = System.currentTimeMillis();
      log.debug("检索 {} 条 花费 {} ms", multiSearchRequest.requests().size(), end - begin);
      return items;
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  public IndexResponse index(IndexRequest indexRequest) {
    try {
      return client.index(indexRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  public DeleteResponse delete(DeleteRequest deleteRequest) {
    try {
      return client.delete(deleteRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  public UpdateResponse update(UpdateRequest updateRequest) {
    try {
      return client.update(updateRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  public BulkResponse bulk(BulkRequest bulkRequest) {
    try {
      return client.bulk(bulkRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new ElasticSearchException(e);
    }
  }

  public BulkProcessor bulkProcessor() {
    BulkProcessor.Listener listener = new BulkProcessor.Listener() {
      @Override
      public void beforeBulk(long executionId, BulkRequest request) {
        int numberOfActions = request.numberOfActions();
        log.debug("Executing bulk [{}] with {} requests", executionId, numberOfActions);
      }

      @Override
      public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
        if (response.hasFailures()) {
          log.error("Bulk [{}] executed with failures", response.getItems()[0].getFailureMessage());
          log.error("Bulk [{}] executed with failures", executionId);
        } else {
          log.debug("Bulk [{}] completed in {} milliseconds", executionId, response.getTook().getMillis());
        }
      }

      @Override
      public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
        log.error("Failed to execute bulk {}", failure);
      }
    };

    BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer =
            (request, bulkListener) -> client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener);
    BulkProcessor.Builder builder = BulkProcessor.builder(bulkConsumer, listener);
    builder.setBulkActions(5000);
    builder.setBulkSize(new ByteSizeValue(10L, ByteSizeUnit.MB));
    builder.setConcurrentRequests(1);
    builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
    builder.setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3));
    return builder.build();
  }

}
