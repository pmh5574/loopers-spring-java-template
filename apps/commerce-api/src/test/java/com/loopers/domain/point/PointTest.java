package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.support.error.CoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PointTest {
    private static final long INITIAL_POINT = 0L;
    private static final long MINIMUM_CHARGE_POINT = 0L;

    @DisplayName("Point 모델을 생성할 때, ")
    @Nested
    class Create {
        @DisplayName("User PK ID가 주어지면 " + INITIAL_POINT + " 포인트가 생성된다.")
        @Test
        void createsPointModel_whenUserModelIdIsProvided() {
            // arrange
            Long userModeId = 1L;

            // act
            Point point = Point.create(userModeId);

            // assert
            assertAll(
                    () -> assertThat(point.getId()).isNotNull(),
                    () -> assertThat(point.getUserModelId()).isEqualTo(userModeId),
                    () -> assertThat(point.getPoint()).isEqualTo(INITIAL_POINT)
            );
        }
    }
    @DisplayName("Point 모델을 충전할 때, ")
    @Nested
    class Charge {
        @DisplayName(MINIMUM_CHARGE_POINT + " 초과의 포인트를 충전하면 정상적으로 충전된다.")
        @Test
        void chargesPoint_whenPositiveAmountProvided() {
            // arrange
            Point pointModel = Point.create(1L);
            Long point = 1L;

            // act
            Long result = pointModel.charge(point);

            // assert
            assertThat(result).isEqualTo(INITIAL_POINT + point);
        }

        @DisplayName(MINIMUM_CHARGE_POINT + " 이하의 포인트를 충전하면 CoreException 예외가 발생한다.")
        @Test
        void throwsCoreException_whenChargeAmountIsInvalid() {
            // arrange
            Point point = Point.create(1L);

            // act & assert
            assertThatThrownBy(() -> point.charge(MINIMUM_CHARGE_POINT))
                    .isInstanceOf(CoreException.class)
                            .hasMessage(MINIMUM_CHARGE_POINT + " 초과의 포인트만 충전이 가능합니다.");
        }
    }
}
