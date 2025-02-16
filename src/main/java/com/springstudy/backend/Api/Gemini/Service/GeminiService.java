package com.springstudy.backend.Api.Gemini.Service;

import com.springstudy.backend.Api.Gemini.Model.Request.GeminiReqDto;
import com.springstudy.backend.Api.Gemini.Model.Response.GeminiResDto;
import com.springstudy.backend.Common.ErrorCode.CustomException;
import com.springstudy.backend.Common.ErrorCode.ErrorCode;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {
    private final RestTemplate restTemplate;
    private final Configuration freemarkerConfig;

    @Value("${gemini.url}")
    private String geminiURL;

    public String chatGemini(String subject){
        GeminiResDto response;
        try{
            GeminiReqDto request = createRequest(subject);
            response = restTemplate.postForObject(geminiURL, request, GeminiResDto.class);
        }catch (RestClientException e){ // RESTTEMPLATE으로 HTTP 요청 시 발생할 수 있는 에러가 발생한 경우.
            System.out.println(e.getMessage());
            throw new CustomException(ErrorCode.RESTTEMPLATE_REQUEST_ERROR);
        }
        String result = response.getCandidates().get(0).getContent().getParts().get(0).getText();

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
}