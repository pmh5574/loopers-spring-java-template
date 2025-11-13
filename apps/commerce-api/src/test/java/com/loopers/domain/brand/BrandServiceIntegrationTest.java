package com.loopers.domain.brand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BrandServiceIntegrationTest {
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @DisplayName("브랜드를 조회할 때,")
    @Nested
    class Get {
        @Test
        void 브랜드_조회시_브랜드_PK_ID를_주면_브랜드_정보가_반환된다() {
            // arrange
            String brandName = "brandTest";
            Brand brand = brandJpaRepository.save(Brand.create(brandName));

            // act
            Brand sut = brandService.getBrand(brand.getId());

            // assert
            assertAll(
                    () -> assertThat(sut.getId()).isNotNull()
            );
        }

        @Test
        void 브랜드_조회시_없는_브랜드_PK_ID를_주면_NOT_FOUND_오류가_발생한다() {
            // arrange
            Long brandId = -1L;

            // act && assert
            assertThatThrownBy(() -> brandService.getBrand(brandId))
                    .isInstanceOfSatisfying(CoreException.class, e -> {
                        assertThat(e.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                    });
        }
    }
}
