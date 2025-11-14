package com.loopers.domain.like;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.vo.Stock;
import com.loopers.infrastructure.like.LikeJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LikeServiceIntegrationTest {
    @Autowired
    private LikeService likeService;
    @Autowired
    private LikeJpaRepository likeJpaRepository;
    @Autowired
    private ProductJpaRepository productJpaRepository;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("좋아요를 생성할 때,")
    @Nested
    class Create {

        @Test
        void 유저_PK_ID와_상품_PK_ID가_주어지면_정상적으로_생성된다() {
            // arrange
            Long userId = 1L;
            Product product = productJpaRepository.save(Product.create("testA", 1000, new Stock(2), 1L));

            // act
            Like like = likeService.createLike(userId, product.getId());

            // assert
            assertAll(
                    () -> assertThat(like.getId()).isNotNull(),
                    () -> assertThat(like.getUserId()).isEqualTo(1L),
                    () -> assertThat(like.getProductId()).isEqualTo(1L)
            );
        }

        @Test
        void 이미_있는_유저_PK_ID와_상품_PK_ID가_주어지면_CONFLICT오류가_발생된다() {
            // arrange
            Long userId = 1L;
            Long productId = 1L;
            likeJpaRepository.save(Like.create(userId, productId));

            // act && assert
            assertThatThrownBy(() -> likeService.createLike(userId, productId))
                    .isInstanceOfSatisfying(CoreException.class, e -> {
                        assertThat(e.getErrorType()).isEqualTo(ErrorType.CONFLICT);
                        assertThat(e.getMessage()).isEqualTo("이미 좋아요를 하셨습니다.");
                    });
        }
    }
}
