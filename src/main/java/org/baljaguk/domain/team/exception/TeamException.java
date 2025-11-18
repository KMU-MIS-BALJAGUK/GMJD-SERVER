package org.baljaguk.domain.team.exception;

import org.baljaguk.global.error.ErrorCode;
import org.baljaguk.global.exception.GlobalException;


public class TeamException extends GlobalException {

    public TeamException(ErrorCode errorCode) {

        super(errorCode);
    }
}