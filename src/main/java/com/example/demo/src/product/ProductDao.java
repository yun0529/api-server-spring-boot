package com.example.demo.src.product;


import com.example.demo.src.product.model.*;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProductList> getProducts(int userNo){
        String getProductsQuery = "select " +
                "Product.productNo," +
                "User.userNo,User.userNickName," +
                "case " +
                "when (User.userRegionNo = 1) then '가좌동' " +
                "when (User.userRegionNo = 2) then '가호동' " +
                "when (User.userRegionNo = 3) then '호탄동' " +
                "when (User.userRegionNo = 4) then null " +
                "when (User.userRegionNo = 10) then '신사동' " +
                "end as userMainRegion, " +
                "case " +
                "when (productTitle != 'null') then productTitle end as productTitle, " +
                "case " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) < 1) then concat(second(now()) - second(Product.createdAt), '초 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) >= 1) then concat(minute(now()) - minute(Product.createdAt), '분 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) >= 1 " +
                ") then concat(hour(now()) - hour(Product.createdAt), '시간 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 1 and dayofyear(now()) - dayofyear(Product.createdAt) < 31 " +
                ") then concat(dayofyear(now()) - dayofyear(Product.createdAt), '일 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 31 " +
                ") then concat(month(now()) - month(Product.createdAt), '개월 전') end as updatedAt, " +
                "productStatus, " +
                "case " +
                "when (productStatus = 'sell'and productPrice = null) then 0 " +
                "when (productStatus = 'reserve'and productPrice = null) then 0 " +
                "when (productStatus = 'sold_out'and productPrice = null) then 0 " +
                "when (productStatus = 'sell' or productStatus = 'reserve' or productStatus = 'sold_out') then format(productPrice,0) " +
                "when (productStatus = 'share' or productStatus = 'shareReserve' or productStatus = 'share_sold_out') then null " +
                "end as productPrice, " +
                "case when (UserProductInterest.productNo = Product.productNo and UserProductInterest.productInterest = 'Y') then count(distinct User.userNo) end as productInterestNum, " +
                "productViewNum, " +
                "count(distinct case when Room.productNo = Product.productNo then 1 end) as chatNum " +
                "from Product " +
                "join User on Product.userNo = User.userNo " +
                "join UserProductInterest " +
                "join Room " +
                "join UserMainRegion on User.userRegionNo = UserMainRegion.userMainRegionNo " +
                "where productTitle != 'null' " +
                "group by Product.productNo " +
                "order by Product.createdAt desc";
        return this.jdbcTemplate.query(getProductsQuery,
                (rs,rowNum) -> new GetProductList(
                    rs.getInt("productNo"),
                    rs.getInt("userNo"),
                    rs.getString("userNickname"),
                    rs.getString("userMainRegion"),
                    rs.getString("productTitle"),
                    rs.getString("updatedAt"),
                    rs.getString("productStatus"),
                    rs.getString("productPrice"),
                    rs.getInt("productInterestNum"),
                    rs.getInt("productViewNum"),
                    rs.getInt("chatNum"))
                );
    }

/*    public List<GetProductList> getProductNosByProductNo(int productNo){
        String getProductsByProductNoQuery = "select " +
                "Product.productNo," +
                "User.userNo,User.userNickName," +
                "case " +
                "when (User.userRegionNo = 1) then '가좌동' " +
                "when (User.userRegionNo = 2) then '가호동' " +
                "when (User.userRegionNo = 3) then '호탄동' " +
                "when (User.userRegionNo = 4) then null " +
                "when (User.userRegionNo = 10) then '신사동' " +
                "end as userMainRegion, " +
                "case " +
                "when (productTitle != 'null') then productTitle end as productTitle, " +
                "case " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) < 1) then concat(second(now()) - second(Product.createdAt), '초 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) >= 1) then concat(minute(now()) - minute(Product.createdAt), '분 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) >= 1 " +
                ") then concat(hour(now()) - hour(Product.createdAt), '시간 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 1 and dayofyear(now()) - dayofyear(Product.createdAt) < 31 " +
                ") then concat(dayofyear(now()) - dayofyear(Product.createdAt), '일 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 31 " +
                ") then concat(month(now()) - month(Product.createdAt), '개월 전') end as updatedAt, " +
                "productStatus, " +
                "case " +
                "when (productStatus = 'sell'and productPrice = null) then 0 " +
                "when (productStatus = 'reserve'and productPrice = null) then 0 " +
                "when (productStatus = 'sold_out'and productPrice = null) then 0 " +
                "when (productStatus = 'sell' or productStatus = 'reserve' or productStatus = 'sold_out') then format(productPrice,0) " +
                "when (productStatus = 'share' or productStatus = 'shareReserve' or productStatus = 'share_sold_out') then null " +
                "end as productPrice, " +
                "case when (UserProductInterest.productNo = Product.productNo and UserProductInterest.productInterest = 'Y') then count(distinct User.userNo) end as productInterestNum, " +
                "productViewNum, " +
                "count(distinct case when Room.productNo = Product.productNo then 1 end) as chatNum " +
                "from Product " +
                "join User on Product.userNo = User.userNo " +
                "join UserProductInterest " +
                "join Room " +
                "join UserMainRegion on User.userRegionNo = UserMainRegion.userMainRegionNo " +
                "where productTitle != 'null' " +
                "group by Product.productNo " +
                "having Product.productNo = ?";
        int getProductByProductNoParams = productNo;
        return this.jdbcTemplate.query(getProductsByProductNoQuery,
                (rs, rowNum) -> new GetProductList(
                        rs.getInt("productNo"),
                        rs.getInt("userNo"),
                        rs.getString("userNickname"),
                        rs.getString("userMainRegion"),
                        rs.getString("productTitle"),
                        rs.getString("updatedAt"),
                        rs.getString("productStatus"),
                        rs.getString("productPrice"),
                        rs.getInt("productInterestNum"),
                        rs.getInt("productViewNum"),
                        rs.getInt("chatNum")),
                getProductByProductNoParams);
    }*/

    public GetProductDetail getProductDetail(int productNo){
        String getProductDetail = "select " +
                "Product.productNo, " +
                "case " +
                "when (Product.productNo = ProductImageUrl.productNo) then productImageUrl end as productImage, " +
                "User.userNo, userImageUrl, userNickname, " +
                "case " +
                "when (userRegionNo = 1) then '가좌동' " +
                "when (userRegionNo = 2) then '가호동' " +
                "when (userRegionNo = 3) then '호탄동' " +
                "when (userRegionNo = 10) then '신사동' " +
                "when (userRegionNo = 4) then 'null' " +
                "end as userMainRegion, " +
                "userManner, productTitle, " +
                "case " +
                "when (productCategory = 1) then '기타 중고물품' " +
                "end as productCategory, " +
                "case " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) < 1) then concat(second(now()) - second(Product.createdAt), '초 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) >= 1) then concat(minute(now()) - minute(Product.createdAt), '분 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) >= 1 " +
                ") then concat(hour(now()) - hour(Product.createdAt), '시간 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 1 and dayofyear(now()) - dayofyear(Product.createdAt) < 31 " +
                ") then concat(dayofyear(now()) - dayofyear(Product.createdAt), '일 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 31 " +
                ") then concat(month(now()) - month(Product.createdAt), '개월 전') end as updatedAt, " +
                "productContent, " +
                "case when (UserProductInterest.productNo = Product.productNo and UserProductInterest.productInterest = 'Y') then count(distinct User.userNo) end as productInterestNum, " +
                "productViewNum, " +
                "count(distinct case when Room.productNo = Product.productNo then 1 end) as chatNum, Product.productStatus, " +
                "case " +
                "when (Product.productStatus = 'sell' and productPrice = null) then 0 " +
                "when (Product.productStatus = 'reserve' and productPrice = null) then 0 " +
                "when (Product.productStatus = 'sole_out'and productPrice = null) then 0 " +
                "when (Product.productStatus = 'sell' or Product.productStatus = 'reserve' or Product.productStatus = 'sold_out') then format(productPrice,0) " +
                "when (Product.productStatus = 'share' or Product.productStatus = 'shareReserve' or Product.productStatus = 'share_sold_out') then null " +
                "end as productPrice, productPriceStatus " +
                "from Product " +
                "join User on Product.userNo = User.userNo " +
                "join UserProductInterest " +
                "join Room " +
                "join ProductImageUrl " +
                "where Product.productNo = ? ";
        int getProductDetailParams = productNo;
        return this.jdbcTemplate.queryForObject(getProductDetail,
                (rs, rowNum) -> new GetProductDetail(
                        rs.getInt("productNo"),
                        rs.getString("productImage"),
                        rs.getInt("userNo"),
                        rs.getString("userImageUrl"),
                        rs.getString("userNickname"),
                        rs.getString("userMainRegion"),
                        rs.getFloat("userManner"),
                        rs.getString("productTitle"),
                        rs.getString("productCategory"),
                        rs.getString("updatedAt"),
                        rs.getString("productContent"),
                        rs.getInt("productInterestNum"),
                        rs.getInt("productViewNum"),
                        rs.getInt("chatNum"),
                        rs.getString("productStatus"),
                        rs.getString("productPrice"),
                        rs.getString("productPriceStatus"))
                        ,
                getProductDetailParams);
    }

    public List<GetInterestProduct> getInterestProduct(int userNo){
        String getInterestProduct = "select " +
                "User.userNo,Product.productNo,User.userNickname, " +
                "case " +
                "when (User.userRegionNo = 1) then '가좌동' " +
                "when (User.userRegionNo = 2) then '가호동' " +
                "when (User.userRegionNo = 3) then '호탄동' " +
                "when (User.userRegionNo = 4) then null " +
                "when (User.userRegionNo = 10) then '신사동' " +
                "end as userMainRegion, " +
                "case " +
                "when (User.userRegionNo < 3 and productTitle != 'null') then productTitle end as productTitle, " +
                "case " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) < 1) then concat(second(now()) - second(Product.createdAt), '초 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) >= 1) then concat(minute(now()) - minute(Product.createdAt), '분 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) >= 1 " +
                ") then concat(hour(now()) - hour(Product.createdAt), '시간 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 1 and dayofyear(now()) - dayofyear(Product.createdAt) < 31 " +
                ") then concat(dayofyear(now()) - dayofyear(Product.createdAt), '일 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 31 " +
                ") then concat(month(now()) - month(Product.createdAt), '개월 전') end as updatedAt, " +
                "productStatus, " +
                "case " +
                "when (productStatus = 'sell'and productPrice = null) then 0 " +
                "when (productStatus = 'reserve'and productPrice = null) then 0 " +
                "when (productStatus = 'sole_out'and productPrice = null) then 0 " +
                "when (productStatus = 'sell' or productStatus = 'reserve' or productStatus = 'sole_out') then format(productPrice,0) " +
                "when (productStatus = 'share' or productStatus = 'shareReserve' or productStatus = 'share_sold_out') then null " +
                "end as productPrice, " +
                "case when (UserProductInterest.productNo = Product.productNo and UserProductInterest.productInterest = 'Y') then count(distinct User.userNo) end as productInterestNum, " +
                "productViewNum, " +
                "count(distinct case when Room.productNo = Product.productNo then 1 end) as chatNum, UserProductInterest.productInterest " +
                "from Product " +
                "join User on Product.userNo = User.userNo " +
                "join Room " +
                "join UserMainRegion " +
                "join UserProductInterest on Product.productNo = UserProductInterest.productNo and UserProductInterest.productInterest = 'Y' " +
                "where UserProductInterest.userNo = ? " +
                "group by Product.productNo";
        int getProductDetailParams = userNo;
        return this.jdbcTemplate.query(getInterestProduct,
                (rs, rowNum) -> new GetInterestProduct(
                        rs.getInt("userNo"),
                        rs.getInt("productNo"),
                        rs.getString("userNickname"),
                        rs.getString("userMainRegion"),
                        rs.getString("productTitle"),
                        rs.getString("updatedAt"),
                        rs.getString("productStatus"),
                        rs.getString("productPrice"),
                        rs.getInt("productInterestNum"),
                        rs.getInt("productViewNum"),
                        rs.getInt("chatNum"),
                        rs.getString("productInterest")
                        )
                ,
                getProductDetailParams);
    }

    public int registInterest(PostInterestReq postInterestReq){
        String createUserQuery = "insert into UserProductInterest (userNo, productNo, productInterest) VALUES (?,?,?)";

        Object[] createUserParams = new Object[]{postInterestReq.getUserNo(), postInterestReq.getProductNo(), "Y"};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkProductInterest(int userNo,int productNo){
        String checkUserIdQuery = "select exists(select userNo, productNo from UserProductInterest where userNo = ? and productNo =?)";
        int checkUserNoParams = userNo;
        int checkProductNoParams = productNo;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserNoParams, checkProductNoParams);
    }

    public int modifyProductInterest(PostInterestReq postInterestReq){
        String modifyUserNameQuery = "update UserProductInterest set productInterest = ? where userNo = ? and productNo =?";
        Object[] modifyUserNameParams = new Object[]{postInterestReq.getProductInterest(), postInterestReq.getUserNo(), postInterestReq.getProductNo()};
        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }
    public int postProduct(int userNo, PostProduct postProduct){
        String createUserQuery = "insert into Product (userNo, productImage, productCategory, productTitle, productPrice, productPriceStatus, productContent, productStatus) VALUES (?,?,?,?,?,?,?,?)";
        System.out.println(postProduct);
        Object[] createUserParams = new Object[]{userNo, postProduct.getProductImage(), postProduct.getProductCategory(), postProduct.getProductTitle(),
                postProduct.getProductPrice(),postProduct.getProductPriceStatus(),postProduct.getProductContent(), postProduct.getProductStatus()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public List<GetSellProduct> getSellProduct(int userNo){
        String getSellProduct = "select " +
                "User.userNo,Product.productNo, productImage, productTitle, " +
                "case " +
                "when (User.userRegionNo = 1) then '가좌동' " +
                "when (User.userRegionNo = 2) then '가호동' " +
                "when (User.userRegionNo = 3) then '호탄동' " +
                "when (User.userRegionNo = 4) then null " +
                "when (User.userRegionNo = 10) then '신사동' " +
                "end as userMainRegion, " +
                "case " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) < 1) then concat(second(now()) - second(Product.createdAt), '초 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) >= 1) then concat(minute(now()) - minute(Product.createdAt), '분 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) >= 1 " +
                ") then concat(hour(now()) - hour(Product.createdAt), '시간 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 1 and dayofyear(now()) - dayofyear(Product.createdAt) < 31 " +
                ") then concat(dayofyear(now()) - dayofyear(Product.createdAt), '일 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 31 " +
                ") then concat(month(now()) - month(Product.createdAt), '개월 전') end as updatedAt, " +
                "format(productPrice,0) as productPrice, productStatus, " +
                "count(distinct case when UserProductInterest.productNo = Product.productNo and UserProductInterest.productInterest = 'Y' then 1 end) as productInterestNum, " +
                "count(distinct case when Room.productNo = Product.productNo then 1 end) as chatNum " +
                "from Product " +
                "join User on Product.userNo = User.userNo " +
                "join UserProductInterest " +
                "join Room " +
                "where User.userNo = ? and productTitle != 'null' and (productStatus = 'sell' or productStatus = 'share' or productStatus = 'reserve' or productStatus = 'share_reserve') " +
                "group by Product.productNo";
        int getSellProductParams = userNo;
        return this.jdbcTemplate.query(getSellProduct,
                (rs, rowNum) -> new GetSellProduct(
                        rs.getInt("userNo"),
                        rs.getInt("productNo"),
                        rs.getString("productImage"),
                        rs.getString("productTitle"),
                        rs.getString("userMainRegion"),
                        rs.getString("updatedAt"),
                        rs.getString("productPrice"),
                        rs.getString("productStatus"),
                        rs.getInt("productInterestNum"),
                        rs.getInt("chatNum")
                )
                ,
                getSellProductParams);
    }
    public List<GetSoldOutProduct> getSoldOutProduct(int userNo){
        String getSoldOutProduct = "select " +
                "User.userNo,Product.productNo, productImage, productTitle, " +
                "case " +
                "when (User.userRegionNo = 1) then '가좌동' " +
                "when (User.userRegionNo = 2) then '가호동' " +
                "when (User.userRegionNo = 3) then '호탄동' " +
                "when (User.userRegionNo = 4) then null " +
                "when (User.userRegionNo = 10) then '신사동' " +
                "end as userMainRegion, " +
                "case " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) < 1) then concat(second(now()) - second(Product.createdAt), '초 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) < 1 and " +
                "minute(now()) - minute(Product.createdAt) >= 1) then concat(minute(now()) - minute(Product.createdAt), '분 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) < 1 and hour(now()) - hour(Product.createdAt) >= 1 " +
                ") then concat(hour(now()) - hour(Product.createdAt), '시간 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 1 and dayofyear(now()) - dayofyear(Product.createdAt) < 31 " +
                ") then concat(dayofyear(now()) - dayofyear(Product.createdAt), '일 전') " +
                "when (year(now()) = year(Product.createdAt) and dayofyear(now()) - dayofyear(Product.createdAt) >= 31 " +
                ") then concat(month(now()) - month(Product.createdAt), '개월 전') end as updatedAt, " +
                "format(productPrice,0) as productPrice, productStatus, " +
                "count(distinct case when UserProductInterest.productNo = Product.productNo and UserProductInterest.productInterest = 'Y' then 1 end) as productInterestNum, " +
                "count(distinct case when Room.productNo = Product.productNo then 1 end) as chatNum " +
                "from Product " +
                "join User on Product.userNo = User.userNo " +
                "join UserProductInterest " +
                "join Room " +
                "where User.userNo = ? and productTitle != 'null' and (productStatus = 'sold_out' or productStatus = 'share_sold_out')" +
                "group by Product.productNo";
        int getSoldOutProductParams = userNo;
        return this.jdbcTemplate.query(getSoldOutProduct,
                (rs, rowNum) -> new GetSoldOutProduct(
                        rs.getInt("userNo"),
                        rs.getInt("productNo"),
                        rs.getString("productImage"),
                        rs.getString("productTitle"),
                        rs.getString("userMainRegion"),
                        rs.getString("updatedAt"),
                        rs.getString("productPrice"),
                        rs.getString("productStatus"),
                        rs.getInt("productInterestNum"),
                        rs.getInt("chatNum"))
                , getSoldOutProductParams);
    }
    public int patchProductStatus(int userNo, PatchProductStatus patchProductStatus){
        String createUserQuery = "update Product set productStatus = ? where userNo = ? and productNo =?";

        Object[] createUserParams = new Object[]{patchProductStatus.getProductStatus(), userNo, patchProductStatus.getProductNo()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}
