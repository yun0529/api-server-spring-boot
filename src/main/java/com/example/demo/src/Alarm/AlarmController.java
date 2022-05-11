package com.example.demo.src.Alarm;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Alarm.model.GetKeyword;
import com.example.demo.src.Alarm.model.PostKeywordReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alarm")
public class AlarmController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AlarmProvider alarmProvider;
    @Autowired
    private final AlarmService alarmService;
    @Autowired
    private final JwtService jwtService;

    public AlarmController(AlarmProvider alarmProvider, AlarmService alarmService, JwtService jwtService){
        this.alarmProvider = alarmProvider;
        this.alarmService = alarmService;
        this.jwtService = jwtService;
    }

    /**
     * 알림 키워드 조회 API
     * [GET] /alarm/keyword/{userNo}
     * @return BaseResponse<List<GetProductList>>
     */
   //Query String
    @ResponseBody
    @GetMapping("/keyword/{userNo}") // (GET) 127.0.0.1:9000/alarm/:userNo
    public BaseResponse<List<GetKeyword>> getKeyword(@PathVariable("userNo") int userNo) {
        try{
            List<GetKeyword> getKeyword = alarmProvider.getKeyword(userNo);
                return new BaseResponse<>(getKeyword);
        } catch(BaseException exception){
            return new BaseResponse<>(((exception.getStatus())));
        }
    }

    /**
     * 알림 키워드 등록 API
     * [POST] /alarm
     * @return BaseResponse<String>
     */
    //Query String
    @ResponseBody
    @PostMapping("/keyword") // (POST) 127.0.0.1:9000/alarm
    public BaseResponse<String> postKeyword(@RequestBody PostKeywordReq postKeywordReq) {
        try{
            String result = "";
            alarmService.postKeyword(postKeywordReq);
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>(((exception.getStatus())));
        }
    }

}
