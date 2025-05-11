package org.personal.comerspleject.domain.users.seller.dto.response;

import lombok.Getter;
import org.personal.comerspleject.domain.users.seller.entity.Product;
import org.personal.comerspleject.domain.users.seller.entity.ProductCategory;

@Getter
public class ProductResponseDto {

    private Long pId;

    private String name;

    private Integer price;

    private ProductCategory category;

    private String imageURL;

    public ProductResponseDto(Long pId, String name, Integer price, ProductCategory category, String imageURL) {
        this.pId = pId;
        this.name = name;
        this.price = price;
        this.category = category;
        this.imageURL = imageURL;
    }

    public static  ProductResponseDto from(Product product) {
        return new ProductResponseDto(
                product.getPId(),
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getImageURL()
        );
    }
}
