package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointFacade;
import com.loopers.application.point.PointInfo;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
@RestController
public class PointV1Controller implements PointV1ApiSpec {
    private final PointFacade pointFacade;

    @Override
    @GetMapping("")
    public ApiResponse<PointResponse> getPoint(@RequestHeader(value = "X-USER-ID", required = false) final Long userModeId) {
        PointInfo pointInfo = pointFacade.getPointByUserModelId(userModeId);
        return ApiResponse.success(PointV1Dto.PointResponse.from(pointInfo));
    }
}
