package com.vasilievpavel.mobhub.rest.model;


import java.util.ArrayList;
import java.util.List;

public class SearchResults<T> {
    private List<T> items = new ArrayList<>();

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
