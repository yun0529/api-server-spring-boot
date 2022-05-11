package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetInterestProduct {
    private int userNo;
    private int productNo;
    private String userNickname;
    private String userMainRegion;
    private String productTitle;
    private String updatedAt;
    private String productStatus;
    private String productPrice;
    private int productInterestNum;
    private int productViewNum;
    private int chatNum;
    private String productInterest;
}
