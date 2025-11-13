package org.baljaguk.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.baljaguk.domain.user.service.UserService;
import org.baljaguk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<Void>> getUser() {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
