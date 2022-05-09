package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.GetProductList;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao, JwtService jwtService) {
        this.productDao = productDao;
        this.jwtService = jwtService;
    }

    public List<GetProductList> getProducts() throws BaseException{
        try{
            List<GetProductList> getProductList = productDao.getProducts();
            return getProductList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProductList> getProductsByProductNo(int productNo) throws BaseException{
        try{
            List<GetProductList> getProductList = productDao.getProductNosByProductNo(productNo);
            return getProductList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

/*
    public GetUserRes getUser(int userNo) throws BaseException {
        try {
            GetUserRes getUserRes = productDao.getUser(userNo);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }*/
}
