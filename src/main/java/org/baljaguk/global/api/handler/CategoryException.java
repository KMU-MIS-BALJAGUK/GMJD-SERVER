package org.baljaguk.global.api.handler;

import org.baljaguk.global.api.ErrorCode;
import org.baljaguk.global.api.GeneralException;

public class CategoryException extends GeneralException {
    public CategoryException(ErrorCode errorCode) {
        super(errorCode);
    }
}
