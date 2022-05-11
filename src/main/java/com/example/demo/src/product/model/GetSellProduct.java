package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSellProduct {
    private int userNo;
    private int productNo;
    private String productImage;
    private String productTitle;
    private String userMainRegion;
    private String updatedAt;
    private String productPrice;
    private String productStatus;
    private int productInterestNum;
    private int chatNum;
}
