package com.recordroom.recordroom.dto;

import lombok.Data;
import java.util.List;

@Data
public class DataTableResponse<T> {
    private int draw;
    private long recordsTotal;
    private long recordsFiltered;
    private List<T> data;
    private String error;
}