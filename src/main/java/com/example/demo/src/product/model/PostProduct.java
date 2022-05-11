package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostProduct {
    private String productImage;
    private String productCategory;
    private String productTitle;
    private String productPrice;
    private String productPriceStatus;
    private String productContent;
    private String productStatus;

}
