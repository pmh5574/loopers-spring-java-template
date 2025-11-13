package com.loopers.infrastructure.product;

import static com.loopers.domain.brand.QBrand.brand;
import static com.loopers.domain.product.QProduct.product;

import com.loopers.domain.product.ProductListDetail;
import com.loopers.domain.product.ProductSortType;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ProductListDetail> getProducts(ProductSortType sortType) {
        return queryFactory
                .select(Projections.constructor(ProductListDetail.class,
                        product.id,
                        product.name,
                        product.price,
                        product.stock.quantity.as("stock"),
                        product.likeCount.value.as("likeCount"),
                        brand.id,
                        brand.name)
                )
                .from(product)
                .join(brand).on(product.brandId.eq(brand.id))
                .orderBy(sortCondition(sortType))
                .fetch();
    }

    private OrderSpecifier<?> sortCondition(ProductSortType sortType) {
        if (sortType == ProductSortType.LATEST) {
            return product.createdAt.desc();
        }
        if (sortType == ProductSortType.PRICE_ASC) {
            return product.price.asc();
        }
        if (sortType == ProductSortType.LIKES_DESC) {
            return product.likeCount.value.desc();
        }

        return null;
    }
}
