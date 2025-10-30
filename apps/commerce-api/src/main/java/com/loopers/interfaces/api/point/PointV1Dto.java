package com.loopers.interfaces.api.point;

import com.loopers.application.point.PointInfo;

public class PointV1Dto {
    public record PointResponse(Long userModelId, Long point) {
        public static PointV1Dto.PointResponse from(PointInfo info) {
            return new PointV1Dto.PointResponse(
                    info.userModelId(),
                    info.point()
            );
        }
    }
}
