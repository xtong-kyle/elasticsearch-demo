package com.example.es.service;

import com.example.es.constant.ESIndices;
import com.example.es.util.ByteConvertUtil;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author: Kyle Tong
 * @Date: 2021/8/6
 */

@Service
public class IndexService {

    private final List<String> INDICES_TO_CREATE = List.of(ESIndices.ITEM_INDEX);
    private final RestHighLevelClient client;
    private static final Logger logger = LoggerFactory.getLogger(IndexService.class);

    @Autowired
    public IndexService(@Qualifier("elasticsearchClient") RestHighLevelClient client) {
        this.client = client;
    }

    @PostConstruct
    public void attemptCreate() {
        final String esSettings = ByteConvertUtil.loadAsString("static/es-settings.json");

        for (final String indexName : INDICES_TO_CREATE) {
            try {
                boolean isIndexExist = client.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
                if (isIndexExist) {
                    continue;
                }
                // if not exist, create one
                final String mappings = ByteConvertUtil.loadAsString("static/mappings/" + indexName + ".json");
                if (esSettings == null || mappings == null) {
                    logger.warn("index created with name '{}'", indexName);
                    continue;
                }
                CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
                createIndexRequest.settings(esSettings, XContentType.JSON);
                createIndexRequest.mapping(mappings, XContentType.JSON);
                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
