package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductDetail {
    private int productNo;
    private String productImage;
    private int userNo;
    private String userImageUrl;
    private String userNickname;
    private String userMainRegion;
    private float userManner;
    private String productTitle;
    private String productCategory;
    private String updatedAt;
    private String productContent;
    private int productInterestNum;
    private int productViewNum;
    private int chatNum;
    private String productStatus;
    private String productPrice;
    private String productPriceStatus;
}
