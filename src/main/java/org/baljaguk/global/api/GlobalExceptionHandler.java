package org.baljaguk.global.api;

import org.baljaguk.global.api.handler.LoginException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.concurrent.RejectedExecutionException;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 공통 예외 핸들러
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(GeneralException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> response = ApiResponse.fail(errorCode.getCode(), errorCode.getMsg());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    // 요청 헤더 누락 핸들러
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingHeader(MissingRequestHeaderException ex) {
        ErrorCode errorCode = ErrorCode.REQUEST_HEADER_EMPTY;
        ApiResponse<Void> response = ApiResponse.fail(errorCode.getCode(), errorCode.getMsg());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    // 잘못된 HTTP 메서드 예외 핸들러
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        ApiResponse<Void> response = ApiResponse.fail(errorCode.getCode(), errorCode.getMsg());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    // 쿼리 파라미터 혹은 PathVariable 타입 예외 핸들러
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        ApiResponse<Void> response = ApiResponse.fail(errorCode.getCode(), errorCode.getMsg());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFound(NoHandlerFoundException ex) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_URL;

        return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.fail(errorCode.getCode(), errorCode.getMsg()));
    }

    // 입력값 검증 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = ErrorCode.NOT_VALID_EXCEPTION;

        return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.fail(errorCode.getCode(), errorCode.getMsg()));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodValidationException(HandlerMethodValidationException e) {
        ErrorCode errorCode = ErrorCode.NOT_VALID_EXCEPTION;

        return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.fail(errorCode.getCode(), errorCode.getMsg()));
    }

    // 기타 모든 예외 (예상치 못한 예외)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnhandledException(Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류입니다."));
    }

    // 클라이언트가 JSON body를 잘못 보냈을 때 Valid로 안잡힌 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        ErrorCode errorCode = ErrorCode.REQUEST_BODY_NOT_READABLE;

        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode.getCode(), errorCode.getMsg()));
    }

    // DB 제약조건 위반
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        ErrorCode errorCode = ErrorCode.DB_CONSTRAINT_VIOLATION;

        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode.getCode(), errorCode.getMsg()));
    }

    // 비동기 요청에서 스레드 풀 크기를 넘어선 경우 예외 발생
    @ExceptionHandler(RejectedExecutionException.class)
    public ResponseEntity<ApiResponse<Void>> handleRejected(RejectedExecutionException e){
        ErrorCode errorCode = ErrorCode.ASYNC_POOL_OVERFLOW;

        return ResponseEntity.status(errorCode.getStatus())
                .body(ApiResponse.fail(errorCode.getCode(), errorCode.getMsg()));
    }

    @ExceptionHandler(LoginException.class)
    public ApiResponse<?> handleLoginException(LoginException e) {
        ErrorCode errorCode = e.getErrorCode();

        return ApiResponse.fail(errorCode.getCode(), errorCode.getMsg());
    }
}
