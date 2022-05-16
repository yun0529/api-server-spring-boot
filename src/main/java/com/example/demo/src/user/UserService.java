package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.*;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED , rollbackFor = Exception.class)
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복
        if(userProvider.checkUserId(postUserReq.getUserId()) ==1){
            throw new BaseException(POST_USERS_EXISTS_NUMBER);
        }
        /*String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getUserPw());
            postUserReq.setUserPw(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }*/

        try{
            int userNo = userDao.createUser(postUserReq);
            //jwt 발급.
            //String jwt = jwtService.createJwt(userNo);
            Random rand  = new Random();
            String randomSum= "";
            for(int i=0; i<4; i++) {
                String ran = Integer.toString(rand.nextInt(10));
                randomSum+=ran;
            }
            int userCode = Integer.parseInt(randomSum);
            return new PostUserRes(userCode,userNo);
        } catch (Exception exception) {
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }

    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public void modifyUserName(PatchUserReq patchUserReq) throws BaseException {
        User user = userDao.getNo(patchUserReq.getUserNo());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            int result = userDao.modifyUserName(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = READ_COMMITTED, rollbackFor = Exception.class)
    public void modifyInterestCategory(PatchInterestCategoryReq patchInterestCategoryReq) throws BaseException {
        User user = userDao.getNo(patchInterestCategoryReq.getUserNo());
        if(user.getStatus().equals("Inactive")){
            throw new BaseException(DO_LOGIN);
        }
        try{
            int result = userDao.modifyInterestCategory(patchInterestCategoryReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            System.out.println(exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String getKaKaoAccessToken(String code){
        String access_Token="";
        String refresh_Token ="";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=ab9b76a86dd12c95755475915f7dc746"); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri=http://localhost:9000/users/login/kakao"); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        }catch (IOException e) {
            System.out.println(e);
        }

        return access_Token;
    }

    public PostCreateKakaoAccountRes createKakaoUser(String token) throws BaseException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        PostCreateKakaoAccountRes postCreateKakaoAccountRes = null;
        String resp = null;
        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            int id = element.getAsJsonObject().get("id").getAsInt();
            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if (hasEmail) {
                email = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
            }

            System.out.println("id : " + id);
            System.out.println("email : " + email);
            postCreateKakaoAccountRes = new PostCreateKakaoAccountRes(
                    element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean(),
                    element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email_needs_agreement").getAsBoolean(),
                    element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString());
//            postCreateKakaoRes = new PostCreateKakaoRes(element.getAsJsonObject().get("id").getAsInt(),
//                    element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("connected_at").getAsString(), postCreateKakaoAccountRes);
            resp = result;
            br.close();
            
        } catch (IOException e) {
            System.out.println(e);
        }
        return postCreateKakaoAccountRes;
    }
}
