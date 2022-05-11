package com.example.demo.src.Alarm;


import com.example.demo.src.Alarm.model.GetKeyword;
import com.example.demo.src.Alarm.model.PostKeywordReq;
import com.example.demo.src.product.model.GetInterestProduct;
import com.example.demo.src.product.model.GetProductDetail;
import com.example.demo.src.product.model.GetProductList;
import com.example.demo.src.product.model.PostInterestReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AlarmDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetKeyword> getKeyword(int userNo){
        String getProductsQuery = "select " +
                "userNo,keyword " +
                "from UserInterestKeyword " +
                "where userNo = ?";
        int getKeywordParam = userNo;
        return this.jdbcTemplate.query(getProductsQuery,
                (rs,rowNum) -> new GetKeyword(
                    rs.getInt("userNo"),
                    rs.getString("keyword")),
                getKeywordParam);
    }

    public int postKeyword(PostKeywordReq postKeywordReq){
        String createUserQuery = "insert into UserInterestKeyword (userNo, keyword) VALUES (?,?)";

        Object[] createUserParams = new Object[]{postKeywordReq.getUserNo(), postKeywordReq.getKeyword()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}
