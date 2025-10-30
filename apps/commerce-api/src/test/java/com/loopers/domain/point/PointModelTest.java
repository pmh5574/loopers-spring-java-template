package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PointModelTest {
    private static final long INITIAL_POINT = 0L;

    @DisplayName("Point 모델을 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("User PK ID가 주어지면 " + INITIAL_POINT + " 포인트가 생성된다.")
        @Test
        void createsPointModel_whenUserModelIdIsProvided() {
            // arrange
            Long userModeId = 1L;

            // act
            PointModel pointModel = PointModel.create(userModeId);

            // assert
            assertAll(
                    () -> assertThat(pointModel.getId()).isNotNull(),
                    () -> assertThat(pointModel.getUserModelId()).isEqualTo(userModeId),
                    () -> assertThat(pointModel.getPoint()).isEqualTo(INITIAL_POINT)
            );
        }
    }
}
