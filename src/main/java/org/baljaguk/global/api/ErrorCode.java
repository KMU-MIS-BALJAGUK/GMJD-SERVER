package org.baljaguk.global.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    /**
     * 400 BAD_REQUEST
     */
    // 인증관련
    REQUEST_HEADER_EMPTY(HttpStatus.BAD_REQUEST, 40000, "요청 헤더가 누락되었습니다."),

    // 입력값 검증 예외
    NOT_VALID_EXCEPTION(HttpStatus.BAD_REQUEST, 40008, "유효하지 않은 입력값입니다."),

    // 클라이언트가 JSON body를 잘못 보냈을 때
    REQUEST_BODY_NOT_READABLE(HttpStatus.BAD_REQUEST, 40016, "요청 데이터 타입이 일치하지 않습니다."),


    /**
     * 401 UNAUTHORIZED
     */
    // 인증관련
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 40100, "유효하지 않은 엑세스 토큰입니다."),


    /**
     * 404 NOT_FOUND
     */
    NOT_FOUND_URL(HttpStatus.NOT_FOUND, 40400, "지원하지 않는 URL입니다."),

    // 구글 엑세스 토큰 관련
    NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE(HttpStatus.BAD_REQUEST, 40401, "구글 액세스 토큰 응답을 찾을 수 없습니다."),

    // 유저 관련
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 40402, "존재하지 않는 사용자입니다."),

    // 카테고리 관련
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, 40403, "카테고리를 찾을 수 없습니다."),

    // 공모전 관련
    NOT_FOUND_CONTEST(HttpStatus.NOT_FOUND, 40404, "공모전을 찾을 수 없습니다."),

    /**
     * 405 METHOD_NOT_ALLOWED
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 40500, "잘못된 HTTP method 요청입니다."),

    /**
     * 429 Too_Many_Requests
     */
    ASYNC_POOL_OVERFLOW(HttpStatus.TOO_MANY_REQUESTS, 42901, "현재 요청이 너무 많습니다. 잠시 후 다시 시도해주세요."),

    /**
     * 500 INTERNAL_SERVER_ERROR
     */
    // DB 제약조건 위반 에러
    DB_CONSTRAINT_VIOLATION(HttpStatus.INTERNAL_SERVER_ERROR, 50018, "DB 제약조건 문제 발생, 서버 개발자에게 문의해주세요"),;


    private final HttpStatus status;
    private final int code;
    private final String msg;

    ErrorCode(HttpStatus status, int code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }
}
