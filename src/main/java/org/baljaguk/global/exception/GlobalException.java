package org.baljaguk.global.exception;

import lombok.Getter;
import org.baljaguk.global.error.ErrorCode;

@Getter
public class GlobalException extends RuntimeException {
    private final ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {

        super(errorCode.getMessage());

        this.errorCode = errorCode;
    }
}