package com.recordroom.recordroom.dto;

import lombok.Data;

@Data
public class DTColumn {
    private String data;
    private String name;
    private boolean searchable;
    private boolean orderable;
    private DTSearch search;
}