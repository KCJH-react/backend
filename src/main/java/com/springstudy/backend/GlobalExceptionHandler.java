//package com.springstudy.backend;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    // 1) NullPointerException 처리
//    @ExceptionHandler(NullPointerException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body("잘못된 요청입니다: " + ex.getMessage());
//    }
//
//    // 2) IllegalArgumentException 처리
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body("입력값 오류: " + ex.getMessage());
//    }
//
//    // 3) 기타 모든 예외 처리
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("서버 오류 발생: " + ex.getMessage());
//    }
//}
//
