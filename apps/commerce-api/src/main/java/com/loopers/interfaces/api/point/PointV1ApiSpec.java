package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.point.PointV1Dto.PointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Point V1 API", description = "Point API 입니다.")
public interface PointV1ApiSpec {

    @Operation(
            summary = "Point 조회",
            description = "Point 조회를 진행합니다."
    )
    ApiResponse<PointResponse> getPoint(
            @Schema(name = "user PK ID", description = "조회할 Point의 User PK ID")
            Long userModeId
    );
}
