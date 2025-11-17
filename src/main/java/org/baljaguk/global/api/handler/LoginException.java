package org.baljaguk.global.api.handler;

import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;

public class LoginException extends GeneralException {
    public LoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
