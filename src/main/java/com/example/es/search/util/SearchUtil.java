package com.example.es.search.util;

import com.example.es.search.SearchRequestDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: Kyle Tong
 * @Date: 2021/8/6
 */
public class SearchUtil {

    private SearchUtil() {

    }

    public static SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO searchRequestDTO) {
        try {
            final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                    .postFilter(getQueryBuilder(searchRequestDTO));
            SearchRequest request = new SearchRequest(indexName);
            request.source(searchSourceBuilder);

            return request;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static QueryBuilder getQueryBuilder(final SearchRequestDTO searchRequestDTO) {
        if (searchRequestDTO == null){
            return null;
        }
        final List<String> fields = searchRequestDTO.getFields();
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }

        // treat multiple fields
        if (fields.size() > 1) {
            MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(searchRequestDTO.getSearchTerm())
                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                    .operator(Operator.AND);
            fields.forEach(queryBuilder::field);
            return queryBuilder;
        }

        return fields.stream().findFirst().map(field -> QueryBuilders.matchQuery(field, searchRequestDTO.getSearchTerm())).orElse(null);
    }
}
