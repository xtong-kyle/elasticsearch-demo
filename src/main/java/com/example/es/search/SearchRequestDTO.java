package com.example.es.search;

import java.util.List;

/**
 * @author: Kyle Tong
 * @Date: 2021/8/6
 */
public class SearchRequestDTO {

    private List<String> fields;
    private String searchTerm;

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }
}
