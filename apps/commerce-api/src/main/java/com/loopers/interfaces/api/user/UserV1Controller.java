package com.loopers.interfaces.api.user;

import com.loopers.application.user.UserFacade;
import com.loopers.application.user.UserInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto.UserSignUpRequest;
import com.loopers.interfaces.api.user.UserV1Dto.UserSignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserV1Controller implements UserV1ApiSpec {

    private final UserFacade userFacade;

    @Override
    @PostMapping("")
    public ApiResponse<UserSignUpResponse> signUp(@RequestBody @Validated final UserSignUpRequest request) {
        UserInfo info = userFacade.singUp(request.toInfo());
        UserV1Dto.UserSignUpResponse response = UserV1Dto.UserSignUpResponse.from(info);
        return ApiResponse.success(response);
    }
}
