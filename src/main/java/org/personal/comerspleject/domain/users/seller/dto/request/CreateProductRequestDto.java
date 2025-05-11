package org.personal.comerspleject.domain.users.seller.dto.request;

import lombok.Getter;
import org.personal.comerspleject.domain.users.seller.entity.ProductCategory;

@Getter
public class CreateProductRequestDto {

    // 상품 등록용
    private String name;

    private String description;

    private Integer price;

    private Integer stock;

    private ProductCategory category;

    private String imageURL;

}
