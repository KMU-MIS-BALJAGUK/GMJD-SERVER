package org.baljaguk.domain.user.service;

import org.baljaguk.domain.user.dto.response.UserRes;
import org.baljaguk.domain.user.entity.User;

public interface UserService {
    UserRes findUser(User user);
}
