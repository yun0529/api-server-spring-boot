package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProductList {
    private int productNo;
    private int userNo;
    private String userNickname;
    private String userMainRegion;
    private String productTitle;
    private String updatedAt;
    private String productStatus;
    private String productPrice;
    private int productInterestNum;
    private int productViewNum;
    private int chatNum;

    public GetProductList(int productNo) {
        this.productNo = productNo;
    }
}
