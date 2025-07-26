package org.personal.comerspleject.domain.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record RecentOrderResponseDto (

    Long orderId,
    LocalDateTime orderDate,
    int totalPrice,
    List<String> productName
) {}
