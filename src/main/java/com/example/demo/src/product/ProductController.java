package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
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
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetProductList>>
     */
   //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetProductList>> getProducts(@RequestParam(required = false) int productNo) {
        try{
            if(productNo == 0){
                List<GetProductList> getProductList = productProvider.getProducts();
                return new BaseResponse<>(getProductList);
            }
            // Get Users
            List<GetProductList> getProductList = productProvider.getProductsByProductNo(productNo);
            return new BaseResponse<>(getProductList);
        } catch(BaseException exception){
            return new BaseResponse<>(((exception.getStatus())));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userNo
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
   /* @ResponseBody
    @GetMapping("/{userNo}") // (GET) 127.0.0.1:9000/app/users/:userNo
    public BaseResponse<GetUserRes> getUser(@PathVariable("userNo") int userNo) {
        // Get Users
        try{
            GetUserRes getUserRes = productProvider.getUser(userNo);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
*/
}
