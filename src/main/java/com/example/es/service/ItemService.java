package com.example.es.service;

import com.example.es.constant.ESIndices;
import com.example.es.model.Item;
import com.example.es.search.SearchRequestDTO;
import com.example.es.search.util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: Kyle Tong
 * @Date: 2021/8/6
 */
@Service
public class ItemService {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final RestHighLevelClient client;
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    public ItemService(@Qualifier("elasticsearchClient") RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * this method is used for match query
     *
     * @param searchRequestDTO
     * @return
     */
    public List<Item> search(final SearchRequestDTO searchRequestDTO) {
        final SearchRequest request = SearchUtil.buildSearchRequest(ESIndices.ITEM_INDEX, searchRequestDTO);
        if (request == null) {
            logger.warn("failed to create search request");
            return Collections.emptyList();
        }

        try {
            final SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
            final SearchHit[] searchHits = searchResponse.getHits().getHits();
            List<Item> searchedItems = new ArrayList<>(searchHits.length);
            for (SearchHit searchHit : searchHits) {
                searchedItems.add(objectMapper.readValue(searchHit.getSourceAsString(), Item.class));
            }
            return searchedItems;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Boolean index(final Item item) {
        try {
            final String itemAsString = objectMapper.writeValueAsString(item);
            final IndexRequest indexRequest = new IndexRequest(ESIndices.ITEM_INDEX);
            indexRequest.id(item.getId());
            indexRequest.source(itemAsString, XContentType.JSON);

            final IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            return indexResponse != null && indexResponse.status().equals(RestStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public Item getById(final String id) {
        try {
            final GetResponse documentFields = client.get(new GetRequest(ESIndices.ITEM_INDEX, id), RequestOptions.DEFAULT);
            if (documentFields == null || documentFields.isSourceEmpty()) {
                logger.warn("document fields not found!");
                return null;
            }
            return objectMapper.readValue(documentFields.getSourceAsString(), Item.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
