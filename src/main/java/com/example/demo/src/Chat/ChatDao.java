package com.example.demo.src.Chat;


import com.example.demo.src.Alarm.model.GetKeyword;
import com.example.demo.src.Alarm.model.PostKeywordReq;
import com.example.demo.src.Chat.model.GetChatContent;
import com.example.demo.src.Chat.model.GetChatRoom;
import com.example.demo.src.Chat.model.PostChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChatDao {

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

    public List<GetChatRoom> getChatRoom(int userNo){
        String getChatRoomQuery = "select " +
                "Room.roomNo,userImageUrl,userNickname as roomTitle, chatContext, " +
                "case " +
                "when (year(now()) = year(Chat.createdAt) and dayofyear(now()) - dayofyear(Chat.createdAt) < 1 and hour(now()) - hour(Chat.createdAt) < 1 and " +
                "minute(now()) - minute(Chat.createdAt) < 1) then concat(second(now()) - second(Chat.createdAt), '초 전') " +
                "when (year(now()) = year(Chat.createdAt) and dayofyear(now()) - dayofyear(Chat.createdAt) < 1 and hour(now()) - hour(Chat.createdAt) < 1 and " +
                "minute(now()) - minute(Chat.createdAt) >= 1) then concat(minute(now()) - minute(Chat.createdAt), '분 전') " +
                "when (year(now()) = year(Chat.createdAt) and dayofyear(now()) - dayofyear(Chat.createdAt) < 1 and hour(now()) - hour(Chat.createdAt) >= 1 " +
                ") then concat(hour(now()) - hour(Chat.createdAt), '시간 전') " +
                "when (year(now()) = year(Chat.createdAt) and dayofyear(now()) - dayofyear(Chat.createdAt) >= 1 and dayofyear(now()) - dayofyear(Chat.createdAt) < 31 " +
                ") then concat(dayofyear(now()) - dayofyear(Chat.createdAt), '일 전') " +
                "when (year(now()) = year(Chat.createdAt) and dayofyear(now()) - dayofyear(Chat.createdAt) >= 31 " +
                ") then concat(month(now()) - month(Chat.createdAt), '개월 전') end as updatedAt " +
                "from Chat " +
                "join Room on Chat.roomNo = Room.roomNo " +
                "join Member on Room.roomNo = Member.roomNo " +
                "join User on Member.userNo = User.userNo and receiveUserNo = User.userNo " +
                "inner join (select roomNo, max(chatNo) as maxChatNo from Chat group by Chat.roomNo) lastMessage " +
                "where maxChatNo = chatNo and (writeUserNo = ? or receiveUserNo = ?) " +
                "order by Chat.createdAt desc";
        Object[] getRoomParams = new Object[]{userNo, userNo};
        return this.jdbcTemplate.query(getChatRoomQuery,
                (rs,rowNum) -> new GetChatRoom(
                        rs.getInt("roomNo"),
                        rs.getString("userImageUrl"),
                        rs.getString("roomTitle"),
                        rs.getString("chatContext"),
                        rs.getString("updatedAt")),
                getRoomParams);
    }
    public List<GetChatContent> getChatContent(int roomNo){
        String getChatRoomQuery = "select " +
                "roomNo, writeUserNo, receiveUserNo,userNickname as writeUserNickname,chatContext, " +
                "date_format(Chat.createdAt, '%Y년 %m월 %d일') as chatDate, date_format(Chat.createdAt, '오후 %H:%i') as chatTime " +
                "from Chat " +
                "join User on Chat.writeUserNo = User.userNo " +
                "where roomNo = ?";
        int getChatContentParams = roomNo;
        return this.jdbcTemplate.query(getChatRoomQuery,
                (rs,rowNum) -> new GetChatContent(
                        rs.getInt("roomNo"),
                        rs.getInt("writeUserNo"),
                        rs.getInt("receiveUserNo"),
                        rs.getString("writeUserNickname"),
                        rs.getString("chatDate"),
                        rs.getString("chatTime")),
                getChatContentParams);
    }
    public int postChat(PostChat postChat){
        String createUserQuery = "insert into Chat (roomNo, writeUserNo, receiveUserNo, chatContext) VALUES (?,?,?,?)";

        Object[] createUserParams = new Object[]{postChat.getRoomNo(), postChat.getWriteUserNo(), postChat.getReceiveUserNo(), postChat.getChatContext()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
}
