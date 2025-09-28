package com.example.demo.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingList<T> {
    private int page;
    private int totalPages;
    private long totalElements;
    private List<T> results;
}
