package com.example.demo.src.Kakao;


import com.example.demo.config.BaseException;
import com.example.demo.src.Kakao.model.KaKaoTokens;
import com.example.demo.src.Kakao.model.PostCreateKakaoAccountRes;
import com.example.demo.src.Kakao.model.PostCreateKakaoRes;
import com.example.demo.utils.JwtService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.demo.config.BaseResponseStatus.*;


// Service Create, Update, Delete 의 로직 처리
@Service
public class KakaoService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KakaoProvider kakaoProvider;
    private final JwtService jwtService;
    private final KakaoDao kakaoDao;
    @Autowired
    public KakaoService(KakaoProvider kakaoProvider, JwtService jwtService, KakaoDao kakaoDao) {
        this.kakaoProvider = kakaoProvider;
        this.jwtService = jwtService;
        this.kakaoDao = kakaoDao;
    }
    public KaKaoTokens getKaKaoAccessToken(String code){
        String access_Token="";
        String refresh_Token ="";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        KaKaoTokens kaKaoTokens = null;
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
            sb.append("&redirect_uri=http://localhost:9000/kakao/login"); // TODO 인가코드 받은 redirect_uri 입력
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
            kaKaoTokens = new KaKaoTokens(access_Token,refresh_Token);
            br.close();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return kaKaoTokens;
    }

    public PostCreateKakaoAccountRes createKakaoUser(String accessToken, String refreshToken) throws BaseException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        PostCreateKakaoAccountRes postCreateKakaoAccountRes = null;
        PostCreateKakaoRes postCreateKakaoRes = null;
        String resp = null;
        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

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
//            postCreateKakaoAccountRes = new PostCreateKakaoAccountRes(
//                    element.getAsJsonObject().get("id").getAsBigInteger(),
//                    token);
            postCreateKakaoAccountRes = new PostCreateKakaoAccountRes(
                    element.getAsJsonObject().get("id").getAsBigInteger(),
                    element.getAsJsonObject().get("connected_at").getAsString(),
                    element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean(),
                    element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString(),"Active");
            postCreateKakaoRes = new PostCreateKakaoRes(
                    element.getAsJsonObject().get("id").getAsBigInteger(),
                    accessToken, refreshToken
            );
            br.close();

        } catch (IOException e) {
            System.out.println(e);
        }
        if(kakaoDao.checkKakaoId(postCreateKakaoAccountRes.getKakaoId()) ==1){
            int statusResult = kakaoDao.modifyKakaoAccountActive(postCreateKakaoAccountRes.getKakaoId());
            if(statusResult == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
            int result = kakaoDao.modifyKakaoAccessToken(postCreateKakaoAccountRes.getKakaoId(),accessToken);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        }else{
            kakaoDao.createKakaoAccount(postCreateKakaoAccountRes);
            kakaoDao.createKakaoToken(postCreateKakaoRes);
        }

        return postCreateKakaoAccountRes;
    }

    public void patchKaKaoAccountInactive(BigInteger kakaoId, String accessToken) throws BaseException {
        PostCreateKakaoAccountRes postCreateKakaoAccountRes = kakaoDao.getId(kakaoId);
        if(postCreateKakaoAccountRes.getStatus().equals("Inactive")){
            throw new BaseException(DO_KAKAO_LOGIN);
        }
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

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

            br.close();
        }catch (IOException e) {
            System.out.println(e);
        }

        try {
            kakaoDao.modifyKakaoLogOut(kakaoId);
        }catch(Exception e){
            System.out.println(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
