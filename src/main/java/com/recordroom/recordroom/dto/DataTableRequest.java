package com.recordroom.recordroom.dto;

import lombok.Data;
import java.util.List;

@Data
public class DataTableRequest {
    private int draw;
    private int start;
    private int length;
    private DTSearch search;
    private List<DTOrder> order;
    private List<DTColumn> columns;
}