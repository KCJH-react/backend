package com.springstudy.backend.Api.Gemini.Service;

import com.springstudy.backend.Api.Gemini.Model.Request.GeminiReqDto;
import com.springstudy.backend.Api.Gemini.Model.Response.GeminiResDto;
import com.springstudy.backend.Api.Repoitory.ChallengeRepository;
import com.springstudy.backend.Api.Repoitory.Entity.Challenge;
import com.springstudy.backend.Common.ErrorCode.CustomException;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import com.springstudy.backend.Common.RedisService;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {
    private final RestTemplate restTemplate;
    private final Configuration freemarkerConfig;
    private final RedisService redisService;
    private final ChallengeRepository challengeRepository;
    String sample = "sample@gmail.com"; // 테스트 단계에서만.

    @Value("${gemini.url}")
    private String geminiURL;

    public String chatGemini(String subject){
        // challenge 생성.
        // 1. subject 입력받음.
        // 2. gemini에 전달해서 랜덤 챌린지 생성.
        GeminiResDto response;
        try{
            GeminiReqDto request = createRequest(subject);
            response = restTemplate.postForObject(geminiURL, request, GeminiResDto.class);
        }catch (RestClientException e){ // RESTTEMPLATE으로 HTTP 요청 시 발생할 수 있는 에러가 발생한 경우.
            System.out.println(e.getMessage());
            throw new CustomException(ErrorCode.RESTTEMPLATE_REQUEST_ERROR);
        }
        String result = response.getCandidates().get(0).getContent().getParts().get(0).getText();
        // challenge 레포 저장.
        // 1. result 정보 추출. 챌린지, 제한시간, 챌린지 생성일.
        // 3. challenge redis에 만료시간 저장.
        // 4. challenge repo에 저장.

        String[] split = result.split(":");
        try{
            saveChallenge(split);
            saveRedisLimitTime(split);
        }
        catch(DataAccessException e){
            throw new CustomException(ErrorCode.ERROR_REDIS_ACCESS);
        }

        return result;
    }

    public GeminiReqDto createRequest(String subject){

        String requestText = "";

        try{
            Template template = freemarkerConfig.getTemplate("prompt.ftl");

            Map<String,String> model = new HashMap<>();
            model.put("subject",subject);
            requestText = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        }
        catch (ParseException e){ // 템플릿에 문법 오류가 있을 때.
            throw new CustomException(ErrorCode.TEMPLATE_PARSEERROR);
        }
        catch(MalformedTemplateNameException e){ // 템플릿 이름이 잘못되었을 때.
            throw new CustomException(ErrorCode.TEMPLATE_NAME_ERROR);
        }
        catch(TemplateException e){ // 템플릿 변수가 제대로 치환되지 않을 때.
            throw new CustomException(ErrorCode.TEMPLATE_VARIABLE_ERROR);
        }
        catch(IOException e){
            throw new CustomException(ErrorCode.TEMPLATE_IO_ERROR);
        }
        if(requestText.equals("")){
            throw new CustomException(ErrorCode.RESPONSE_NULL);
        }
        GeminiReqDto request = new GeminiReqDto();
        request.createGeminiReqDto(requestText);
        return request;
    }

    public void saveChallenge(String[] split) throws DataAccessException {
        LocalDateTime localDateTime = LocalDateTime.now();
        Challenge challenge = Challenge.builder()
                .challengeTitle(split[0])
                .limitTime(split[1])
                .current(localDateTime)
                .build();
        Challenge save = challengeRepository.save(challenge);
        if(save == null){
            //todo error
        }
    }
    public void saveRedisLimitTime(String[] split) throws DataAccessException{
        try{
            redisService.setDataExpire(sample,split[0], Long.parseLong(split[1].trim().replaceAll("[^0-9]", "")));
        }
        catch (NumberFormatException e){
            throw new CustomException(ErrorCode.NUMBER_FORMAT_ERROR);
        }
        System.out.println(sample+": "+redisService.getData(sample)+redisService.getExpire(sample));
    }

    public ErrorCode clearChallenge(String challenge){
        // 챌린지 확인.
        // 1. redis에서 현재 사용자와 챌린지로 확인.
        // 2. 있으면 redis에서 삭제하고 성공 처리.
        try{
            String redisChallenge = redisService.getData(sample);
//            System.out.println("전"+redisChallenge.trim()+":"+challenge.equals(redisChallenge.trim()));
//            if(!challenge.trim().equals(redisChallenge.trim())){
//                throw new CustomException(ErrorCode.NOT_EXIST_CHALLENGE);
//            }

            boolean deleted = redisService.deleteData(sample);
            System.out.println("삭제확인: "+deleted);
            if(!deleted){
                throw new CustomException(ErrorCode.NOT_DELETE_CHALLENGE);
            }
        }
        catch (DataAccessException e){
            throw new CustomException(ErrorCode.ERROR_REDIS_ACCESS);
        }
        catch (Exception e) {
            //throw new CustomException(e.getMessage(), ErrorCode.FAILURE);
            System.out.println(e.getMessage()+"\n"+e.toString());
        }


        System.out.println("Redis 삭제 후 확인: " + redisService.getData(sample));
        return ErrorCode.SUCCESS;
    }
}