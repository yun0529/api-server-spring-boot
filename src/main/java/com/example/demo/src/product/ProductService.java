package com.example.demo.src.product;


import com.example.demo.config.BaseException;
import com.example.demo.src.product.model.PatchProductStatus;
import com.example.demo.src.product.model.PostInterestReq;
import com.example.demo.src.product.model.PostProduct;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.src.user.model.User;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ProductService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProductDao productDao;
    private final ProductProvider productProvider;
    private final JwtService jwtService;
    private final UserDao userDao;
    @Autowired
    public ProductService(ProductDao productDao, ProductProvider productProvider, JwtService jwtService, UserDao userDao) {
        this.productDao = productDao;
        this.productProvider = productProvider;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public void registInterest(PostInterestReq postInterestReq) throws BaseException {
        User user = userDao.getNo(postInterestReq.getUserNo());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
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
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public void postProduct(int userNo, PostProduct postProduct) throws BaseException {
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            productDao.postProduct(userNo, postProduct);

        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public void patchProductStatus(int userNo, PatchProductStatus patchProductStatus) throws BaseException {
        User user = userDao.getNo(userNo);
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            productDao.patchProductStatus(userNo, patchProductStatus);

        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
