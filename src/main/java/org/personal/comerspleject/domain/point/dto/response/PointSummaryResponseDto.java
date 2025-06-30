package org.personal.comerspleject.domain.point.dto.response;

public class PointSummaryResponseDto {

    private int totalPoint;

    private int totalEarned;

    private int totalUesd;

    public PointSummaryResponseDto(int totalPoint, int totalEarned, int totalUesd) {
        this.totalPoint = totalPoint;
        this.totalEarned = totalEarned;
        this.totalUesd = totalUesd;
    }

}
