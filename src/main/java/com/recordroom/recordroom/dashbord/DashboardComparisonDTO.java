package com.recordroom.recordroom.dashbord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardComparisonDTO {
    private String label;
    private Long fromValue;
    private Long toValue;
    private Long difference;

    public DashboardComparisonDTO(String label, Long fromValue, Long toValue) {
        this.label = label;
        this.fromValue = (fromValue == null) ? 0L : fromValue;
        this.toValue = (toValue == null) ? 0L : toValue;
        this.difference = this.toValue - this.fromValue;
    }
}