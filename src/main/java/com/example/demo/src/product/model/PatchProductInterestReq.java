package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchProductInterestReq {
    private int userNo;
    private int productNo;
    private String productInterest;
}
