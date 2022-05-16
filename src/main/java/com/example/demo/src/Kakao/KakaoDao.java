package com.example.demo.src.Kakao;


import com.example.demo.src.Kakao.model.PostCreateKakaoAccountRes;
import com.example.demo.src.Kakao.model.PostCreateKakaoRes;
import com.example.demo.src.user.model.PatchUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.Random;

@Repository
public class KakaoDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createKakaoAccount(PostCreateKakaoAccountRes postCreateKakaoAccountRes){
        String createUserQuery = "insert into KakaoAccount (kakaoId, connectedAt, hasEmail, email, status) VALUES (?,?,?,?,?)";

        Object[] createUserParams = new Object[]{
                postCreateKakaoAccountRes.getKakaoId(),
                postCreateKakaoAccountRes.getConnectedAt(),
                postCreateKakaoAccountRes.getHasEmail(),
                postCreateKakaoAccountRes.getEmail(),
                postCreateKakaoAccountRes.getStatus()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int createKakaoToken(PostCreateKakaoRes postCreateKakaoRes){
        String createUserQuery = "insert into KakaoToken (kakaoId, accessToken) VALUES (?,?)";

        Object[] createUserParams = new Object[]{
                postCreateKakaoRes.getId(), postCreateKakaoRes.getAccessToken()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checKakaoId(BigInteger kakaoId){
        String checkUserIdQuery = "select exists(select accessToken from KakaoToken where kakaoId = ?)";
        BigInteger checkKakaoIdParams = kakaoId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkKakaoIdParams);
    }

    public int modifyKakaoAccessToken(BigInteger kakaoId,String accessToken){
        String modifyUserNameQuery = "update KakaoAccount set accessToken = ? where kakaoId = ? ";
        Object[] modifyUserNameParams = new Object[]{accessToken, kakaoId};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }
}
