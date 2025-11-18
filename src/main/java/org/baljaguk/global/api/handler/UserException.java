package org.baljaguk.global.api.handler;

import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;

public class UserException extends GeneralException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
