package org.personal.comerspleject.domain.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

// 클라이언트로부터 데이터를 받을 떄 사용되는 dto
@Getter
@NoArgsConstructor // jackson이 json -> 객체로 역직렬화 할 때 필요
public class OrderRequestDto {

    // 클라이언트가 보낸 주문 전체 요청 정보
    private Long userId;

    private List<OrderItemRequestDto> items;

}
