package com.springstudy.backend.Common;

import com.springstudy.backend.Error;
import com.springstudy.backend.ErrorResponsev2;
import com.springstudy.backend.Response;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Slf4j
public class ResponseBuilder<T> {
    private ErrorResponsev2 errorResponsev2;
    private T data;
    private HttpStatus status;

    public static <T> ResponseBuilder<T> create(){
        return new ResponseBuilder<>();
    }

    public ResponseBuilder<T> data(T data){
        this.data = data;
        return this;
    }
    public ResponseBuilder<T> errorResponsev2(Error code, String message){
        if(code == null) log.info(message);
        else { log.error(message); this.errorResponsev2 = new ErrorResponsev2(code, message);}
        return this;
    }
    public ResponseBuilder<T> status(HttpStatus status){
        this.status = status;
        return this;
    }
    public ResponseEntity<Response<T>> build(){
        Response<T> response = new Response<>();
        response.setData(data);
        response.setErrorResponsev2(errorResponsev2);
        return new ResponseEntity<>(response, status);
    }
}
