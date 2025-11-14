package com.loopers.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.product.vo.Stock;
import com.loopers.infrastructure.brand.BrandJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceIntegrationTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductJpaRepository productJpaRepository;
    @Autowired
    private BrandJpaRepository brandJpaRepository;
    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품을 조회할 때,")
    @Nested
    class Get {

        @Test
        void 상품_조회시_상품_PK_ID를_주면_상품_정보가_반환된다() {
            // arrange
            String productName = "productTest";
            Integer price = 10000;
            Stock stock = new Stock(10);
            Long brandId = 1L;
            Product product = productJpaRepository.save(Product.create(productName, price, stock, brandId));

            // act
            Product sut = productService.getProduct(product.getId());

            // assert
            assertAll(
                    () -> assertThat(sut.getId()).isEqualTo(product.getId())
            );
        }

        @Test
        void 상품_조회시_없는_상품_PK_ID를_주면_NOT_FOUND_오류가_발생한다() {
            // arrange
            Long productId = -1L;

            // act && assert
            assertThatThrownBy(() -> productService.getProduct(productId))
                    .isInstanceOfSatisfying(CoreException.class, e -> {
                        assertThat(e.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
                    });
        }

        @Test
        void 상품_상세_조회시_상품_PK_ID를_주면_상품_정보와_브랜드_좋아요수가_포함된다() {
            // arrange
            String brandName = "brandTest";
            Brand brand = brandJpaRepository.save(Brand.create(brandName));
            String productName = "productTest";
            Integer price = 10000;
            Stock stock = new Stock(10);
            Product product = productJpaRepository.save(Product.create(productName, price, stock, brand.getId()));

            // act
            ProductDetail sut = productService.getProductDetail(product.getId());

            // assert
            assertAll(
                    () -> assertThat(sut.product().getId()).isEqualTo(product.getId()),
                    () -> assertThat(sut.brand().getId()).isEqualTo(brand.getId())
            );
        }

        @Test
        void 상품_리스트_조회시_최근순_정렬() {
            // arrange
            Brand brand = brandJpaRepository.save(Brand.create("brandTest"));
            productJpaRepository.save(Product.create("testA", 1000, new Stock(2), brand.getId()));
            productJpaRepository.save(Product.create("testB", 2000, new Stock(2), brand.getId()));
            ProductSortType productSortType = ProductSortType.LATEST;

            // act
            ProductList products = productService.getProducts(productSortType);

            assertThat(products.productListDetails()).hasSize(2)
                    .extracting(ProductListDetail::name)
                    .containsExactly("testB", "testA");
        }

        @Test
        void 상품_리스트_조회시_가격_낮은순_정렬() {
            // arrange
            Brand brand = brandJpaRepository.save(Brand.create("brandTest"));
            productJpaRepository.save(Product.create("testA", 1000, new Stock(2), brand.getId()));
            productJpaRepository.save(Product.create("testB", 2000, new Stock(2), brand.getId()));
            ProductSortType productSortType = ProductSortType.PRICE_ASC;

            // act
            ProductList products = productService.getProducts(productSortType);

            assertThat(products.productListDetails()).hasSize(2)
                    .extracting(ProductListDetail::price)
                    .containsExactly(1000, 2000);
        }

        @Test
        void 상품_리스트_조회시_좋아요_많은순_정렬() {
            // arrange
            Brand brand = brandJpaRepository.save(Brand.create("brandTest"));
            productJpaRepository.save(Product.create("testA", 1000, new Stock(2), brand.getId()));
            Product testB = Product.create("testB", 2000, new Stock(2), brand.getId());
            testB.likeCountIncrease(30);
            productJpaRepository.save(testB);
            ProductSortType productSortType = ProductSortType.LIKES_DESC;

            // act
            ProductList products = productService.getProducts(productSortType);

            assertThat(products.productListDetails()).hasSize(2)
                    .extracting(ProductListDetail::name)
                    .containsExactly("testB", "testA");
        }

        @Test
        void 상품_조회시_LIST_상품_PK_ID를_주면_상품_LIST가_반환된다() {
            // arrange
            Product p1 = productJpaRepository.save(Product.create("p1", 10_000, new Stock(10), 1L));
            Product p2 = productJpaRepository.save(Product.create("p2", 20_000, new Stock(5), 1L));

            // act
            List<Product> sut = productService.getProductIn(List.of(p1.getId(), p2.getId()));

            // assert
            assertThat(sut).hasSize(2);
        }
    }

    @DisplayName("좋아요 수를 증가시킬 때,")
    @Nested
    class LikeCount {

        @Test
        void 상품의_좋아요수가_증가한다() {
            // arrange
            Product product = productJpaRepository.save(Product.create("test", 1000, new Stock(1), 1L));

            // act
            productService.likeCountIncrease(product.getId(), 5);

            // assert
            Product sut = productJpaRepository.findById(product.getId()).orElse(null);
            assertThat(sut.getLikeCount().getValue()).isEqualTo(5);
        }

        @Test
        void 상품의_좋아요수가_감소한다() {
            // arrange
            Product product = productJpaRepository.save(Product.create("test", 1000, new Stock(1), 1L));
            product.likeCountIncrease(5);
            productJpaRepository.save(product);

            // act
            productService.likeCountDecrease(product.getId(), 1);

            // assert
            Product sut = productJpaRepository.findById(product.getId()).orElse(null);
            assertThat(sut.getLikeCount().getValue()).isEqualTo(4);
        }
    }
}
