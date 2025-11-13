package com.fpt.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatisticsDTO {
    private String period; // "day", "month", "year"
    private Long totalUsers;
    private Long activeUsers; // Users có đơn hàng trong period
    private List<UserDataPoint> newUsersByPeriod;
    private Map<String, Long> usersByRole;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserDataPoint {
        private String date; // Format depends on period
        private Long newUserCount;
    }
}

