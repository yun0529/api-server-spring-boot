package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.GetProductDetail;
import com.example.demo.src.product.model.GetProductList;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexUserId;

@RestController
@RequestMapping("/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    /**
     * 상품 조회 API
     * [GET] /product
     * @return BaseResponse<List<GetProductList>>
     */
   //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetProductList>> getProducts() {
        try{
            //if(productNo == 0){
                List<GetProductList> getProductList = productProvider.getProducts();
                return new BaseResponse<>(getProductList);
            //}
            // Get Users
            //List<GetProductList> getProductList = productProvider.getProductsByProductNo(productNo);
            //return new BaseResponse<>(getProductList);
        } catch(BaseException exception){
            return new BaseResponse<>(((exception.getStatus())));
        }
    }

    /**
     * 상품 세부 조회 API
     * [GET] /product/:productNo
     * @return BaseResponse<GetProductList>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{productNo}") // (GET) 127.0.0.1:9000/products/:productNo
    public BaseResponse<GetProductDetail> getProductDetail(@PathVariable("productNo") int productNo) {
        // Get Users
        try{
            GetProductDetail getProductDetail = productProvider.getProductDetail(productNo);
            return new BaseResponse<>(getProductDetail);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }


}
