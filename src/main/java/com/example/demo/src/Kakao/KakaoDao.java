package com.example.demo.src.Kakao;


import com.example.demo.src.Kakao.model.PostCreateKakaoAccountRes;
import com.example.demo.src.Kakao.model.PostCreateKakaoRes;
import com.example.demo.src.Kakao.model.PostKakaoJwt;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostCertificationUserReq;
import com.example.demo.src.user.model.User;
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
        String createUserQuery = "insert into KakaoToken (kakaoId, accessToken, refreshToken) VALUES (?,?,?)";

        Object[] createUserParams = new Object[]{
                postCreateKakaoRes.getKakaoId(), postCreateKakaoRes.getAccessToken(),postCreateKakaoRes.getRefreshToken()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkKakaoId(BigInteger kakaoId){
        String checkUserIdQuery = "select exists(select accessToken from KakaoToken where kakaoId = ?)";
        BigInteger checkKakaoIdParams = kakaoId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkKakaoIdParams);
    }

    public int modifyKakaoAccessToken(BigInteger kakaoId,String accessToken){
        String modifyUserNameQuery = "update KakaoToken set accessToken = ? where kakaoId = ? ";
        Object[] modifyUserNameParams = new Object[]{accessToken, kakaoId};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyKakaoAccountActive(BigInteger kakaoId){
        String modifyUserNameQuery = "update KakaoAccount set status = ? where kakaoId = ? ";
        Object[] modifyUserNameParams = new Object[]{"Active",kakaoId};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int postKakaoJwt(int userNo, String kakaoJwt){
        String createUserQuery = "insert into KakaoJwt (userNo, kakaoJwt) VALUES (?,?)";

        Object[] createUserParams = new Object[]{userNo, kakaoJwt};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
    public int checkKakaoJwt(int userNo){
        String checkUserIdQuery = "select exists(select userNo from KakaoJwt where userNo = ?)";
        int checkUserNoParams = userNo;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserNoParams);
    }
    public int modifyKakaoJwt(int userNo, String kakaoJwt){
        String modifyUserNameQuery = "update KakaoJwt set kakaoJwt = ? where userNo = ? ";
        Object[] modifyUserNameParams = new Object[]{kakaoJwt,userNo};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }
    public PostCreateKakaoAccountRes getId(BigInteger kakaoId){
        String getIdQuery = "select kakaoId, connectedAt, hasEmail, email, status from KakaoAccount where kakaoId = ? ";
        BigInteger getIdParams = kakaoId;
        return this.jdbcTemplate.queryForObject(getIdQuery,
                (rs,rowNum)-> new PostCreateKakaoAccountRes(
                        kakaoId,
                        rs.getString("connectedAt"),
                        rs.getBoolean("hasEmail"),
                        rs.getString("email"),
                        rs.getString("status")
                ),
                getIdParams);
    }
    public int modifyKakaoLogOut(BigInteger kakaoId){
        String modifyUserNameQuery = "update KakaoAccount set status = ? where kakaoId = ? ";
        Object[] modifyUserNameParams = new Object[]{"Inactive", kakaoId};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }
}
