package org.baljaguk.global.api.handler;

import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;

public class TokenException extends GeneralException {
    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
