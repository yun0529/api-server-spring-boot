package com.example.demo.src.product;


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

    public List<GetProductList> getProductNosByProductNo(int productNo){
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
    }
/*
    public GetUserRes getUser(int userNo){
        String getUserQuery = "select userNo,userImageUrl,userNickname,userCode,userManner,userRedealRate,userResponseRate, " +
        "date_format(User.createdAt,'%Y년 %m월 %d일 가입') as createdAt, "+
        "case when (year(User.updatedAt) = year(now()) and dayofyear(now())- dayofyear(User.updatedAt) <= 3) then '최근 3일 이내 활동' " +
        "when (year(now()) - year(User.updatedAt) = 1) then '최근 1년 이내 활동' " +
        "when (year(now()) - year(User.updatedAt) <= 5) then '최근 5년 이내 활동' " +
        "end as updatedAt, User.status, "+
        "case " +
            "when (userRegionNo = '1') then '가좌동' " +
            "when (userRegionNo = '2') then '가호동' " +
            "when (userRegionNo = '3') then '호탄동' " +
            "when (userRegionNo = '4') then null " +
            "when (userRegionNo = '10') then '신사동' " +
            "end as userRegionNo " +
            ", userRegionCertificationNum, " +
        "case " +
            "when (User.userSubRegionNo = '1') then '가좌동' " +
            "when (User.userSubRegionNo = '2') then '가호동' " +
            "when (User.userSubRegionNo = '3') then '호탄동' " +
            "when (User.userSubRegionNo = '4') then null " +
            "when (User.userSubRegionNo = '10') then '신사동' " +
            "end as userSubRegionNo " +
            ", userSubRegionCertificationNum, " +
        "case " +
            "when (year(UserMainRegion.updatedAt) = year(now()) and dayofyear(now())- dayofyear(UserMainRegion.updatedAt) <= 30 and userRegionCertificationNum >= 1) then '최근 30일' " +
            "when (year(now()) - year(UserMainRegion.updatedAt) = 1) then '최근 1년 이내' " +
            "else '미인증' end as UserMainRegionUpdatedDate " +
        "from User "+
        "join UserMainRegion on User.userRegionNo = UserMainRegion.userMainRegionNo " +
        "join UserSubRegion on UserMainRegion.userSubRegionNo = UserSubRegion.userSubRegionNo " +
        "where User.userNo = ?";
        int getUserParams = userNo;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userNo"),
                        rs.getString("userImageUrl"),
                        rs.getString("userNickname"),
                        rs.getInt("userCode"),
                        rs.getFloat("userManner"),
                        rs.getString("userRedealRate"),
                        rs.getString("userResponseRate"),
                        rs.getString("createdAt"),
                        rs.getString("updatedAt"),
                        rs.getString("status"),
                        rs.getString("userRegionNo"),
                        rs.getInt("userRegionCertificationNum"),
                        rs.getString("userSubRegionNo"),
                        rs.getInt("userSubRegionCertificationNum"),
                        rs.getString("UserMainRegionUpdatedDate"))
                        ,
                getUserParams);
    }*/

}
