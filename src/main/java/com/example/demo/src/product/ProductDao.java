package com.example.demo.src.product;


import com.example.demo.src.product.model.GetProductDetail;
import com.example.demo.src.product.model.GetProductList;
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

    public List<GetProductList> getProducts(){
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

}
