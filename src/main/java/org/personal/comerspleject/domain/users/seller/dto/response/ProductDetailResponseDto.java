package org.personal.comerspleject.domain.users.seller.dto.response;

import lombok.Getter;
import org.personal.comerspleject.domain.users.seller.entity.Product;
import org.personal.comerspleject.domain.users.seller.entity.ProductCategory;

@Getter
public class ProductDetailResponseDto {

    private Long pId;
    private String name;
    private String description;
    private Integer price;
    private Integer stock;
    private ProductCategory category;
    private String imageURL;
    private String sellerName;

    public ProductDetailResponseDto(Product product) {
        this.pId = product.getPId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.category = product.getCategory();
        this.imageURL = product.getImageURL();
        this.sellerName = product.getSeller().getName();
    }

}
