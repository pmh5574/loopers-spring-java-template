package com.loopers.application.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointFacadeIntegrationTest {
    @Autowired
    private PointFacade pointFacade;
    @Autowired
    private PointJpaRepository pointJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("포인트를 조회할 때, ")
    @Nested
    class Get {

        @DisplayName("존재하는 user ID를 주면, 해당 포인트 정보를 반환한다.")
        @Test
        void returnsPointInfo_whenValidUserModelIdIsProvided() {
            // arrange
            String userId = "test";
            String email = "test@test.com";
            LocalDate birthday = LocalDate.of(2020, 1, 1);
            UserModel saveUserModel = UserModel.create(userId, email, birthday, Gender.MALE);
            UserModel userModel = userJpaRepository.save(saveUserModel);

            // act
            PointInfo result = pointFacade.getPointByUserModelId(userModel.getId());

            // assert
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.id()).isNotNull(),
                    () -> assertThat(result.userModelId()).isEqualTo(userModel.getId()),
                    () -> assertThat(result.point()).isEqualTo(0L)
            );
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnsNull_whenUserDoesNotExist() {
            // arrange
            Long userModelId = -1L;

            // act
            PointInfo result = pointFacade.getPointByUserModelId(userModelId);

            // assert
            assertThat(result).isNull();
        }
    }

}
