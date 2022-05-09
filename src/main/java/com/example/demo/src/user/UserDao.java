package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Random;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                    rs.getInt("userNo"),
                    rs.getString("userImageUrl"),
                    rs.getString("userNickname"),
                    rs.getInt("userCode"),
                    rs.getFloat("userManner"),
                    rs.getString("userRedealRate"),
                    rs.getString("userResponseRate"),
                    rs.getString("createdDate"),
                    rs.getString("updatedDate"),
                    rs.getString("status"),
                    rs.getString("userRegionNo"),
                    rs.getInt("userRegionCertificationNum"),
                    rs.getString("userSubRegion"),
                    rs.getInt("userSubRegionCertification"),
                    rs.getString("UserMainRegionUpdatedDate"))
                );
    }

    public List<GetUserRes> getUsersByUserNo(int userNo){
        String getUsersByEmailQuery = "select * from User where userNo =?";
        int getUsersByEmailParams = userNo;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userNo"),
                        rs.getString("userImageUrl"),
                        rs.getString("userNickname"),
                        rs.getInt("userCode"),
                        rs.getFloat("userManner"),
                        rs.getString("userRedealRate"),
                        rs.getString("userResponseRate"),
                        rs.getString("createdDate"),
                        rs.getString("updatedDate"),
                        rs.getString("status"),
                        rs.getString("userRegionNo"),
                        rs.getInt("userRegionCertificationNum"),
                        rs.getString("userSubRegion"),
                        rs.getInt("userSubRegionCertification"),
                        rs.getString("UserMainRegionUpdatedDate")),
                getUsersByEmailParams);
    }

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
    }

    public GetMyCarrot getMyCarrotByUserNo(int userNo){
        String getGetMyCarrotByUserNoQuery = "select " +
                "userNo,userImageUrl,userNickname,userCode " +
                "from User " +
                "where userNo = ?";
        int getMyCarrotByUserNoParams = userNo;
        return this.jdbcTemplate.queryForObject(getGetMyCarrotByUserNoQuery,
                (rs, rowNum) -> new GetMyCarrot(
                        rs.getInt("userNo"),
                        rs.getString("userImageUrl"),
                        rs.getString("userNickname"),
                        rs.getInt("userCode")),
                getMyCarrotByUserNoParams);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (userId, userPw, userRegionNo,userCode,userNickname) VALUES (?,?,?,?,?)";
        Random rand  = new Random();
        String randomSum= "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            randomSum+=ran;
        }
        int userCode = Integer.parseInt(randomSum);
        Object[] createUserParams = new Object[]{postUserReq.getUserId(), postUserReq.getUserPw(), postUserReq.getUserRegionNo(),userCode,"당근 유저"};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkUserId(String userId){
        String checkUserIdQuery = "select exists(select userId from User where userId = ?)";
        String checkUserIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkUserIdQuery,
                int.class,
                checkUserIdParams);
    }


    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set userNickname = ? where userNo = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserNickname(), patchUserReq.getUserNo()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userNo, userId, userPw, userNickname, status from User where userId = ?";
        String getPwdParams = postLoginReq.getUserId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userNo"),
                        rs.getString("userId"),
                        rs.getString("userPw"),
                        rs.getString("userNickname"),
                        rs.getString("status")
                ),
                getPwdParams
                );

    }
    public int modifyUserStatusLogIn(PostLoginReq postLoginReq){
        String modifyUserNameQuery = "update User set status = ? where userId = ? ";
        Object[] modifyUserNameParams = new Object[]{"Active", postLoginReq.getUserId()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

}
