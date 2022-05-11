package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PatchProductStatus;
import com.example.demo.src.product.model.PostInterestReq;
import com.example.demo.src.product.model.PostProduct;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService;

    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService = jwtService;
    }

    public void registInterest(PostInterestReq postInterestReq) throws BaseException {
        try{
            if(productProvider.checkInterestProduct(postInterestReq.getUserNo(), postInterestReq.getProductNo()) ==1){
                productDao.modifyProductInterest(postInterestReq);
            }
            else{
                productDao.registInterest(postInterestReq);
            }

        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void postProduct(int userNo, PostProduct postProduct) throws BaseException {
        try{
            productDao.postProduct(userNo, postProduct);

        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }
    public void patchProductStatus(int userNo, PatchProductStatus patchProductStatus) throws BaseException {
        try{
            productDao.patchProductStatus(userNo, patchProductStatus);

        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
