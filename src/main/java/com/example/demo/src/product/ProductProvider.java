package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.UserDao;
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

    private final UserDao userDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ProductProvider(ProductDao productDao, JwtService jwtService, UserDao userDao) {
        this.productDao = productDao;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    public List<GetProductList> getProducts(int userNo) throws BaseException{
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            List<GetProductList> getProductList = productDao.getProducts(userNo);
            return getProductList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetProductDetail getProductDetail(int productNo, int userNo) throws BaseException {
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try {
            GetProductUser getProductUser = productDao.getUserNo(productNo);
            GetProductDetail getProductDetail = productDao.getProductDetail(productNo,getProductUser.getUserNo());
            return getProductDetail;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkInterestProduct(int userNo, int productNo) throws BaseException{
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            return productDao.checkProductInterest(userNo, productNo);
        } catch (Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetInterestProduct> getInterestProduct(int userNo) throws BaseException {
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try {
            List<GetInterestProduct> getInterestProduct = productDao.getInterestProduct(userNo);
            return getInterestProduct;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetSellProduct> getSellProduct(int userNo) throws BaseException {
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try {
            List<GetSellProduct> getSellProduct = productDao.getSellProduct(userNo);
            return getSellProduct;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetSoldOutProduct> getSoldOutProduct(int userNo) throws BaseException {
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try {
            List<GetSoldOutProduct> getSoldOutProduct = productDao.getSoldOutProduct(userNo);
            return getSoldOutProduct;
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
