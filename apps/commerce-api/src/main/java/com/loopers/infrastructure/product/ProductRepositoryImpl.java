package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductListDetail;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.product.ProductSortType;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;

    @Override
    public Optional<Product> findById(final Long productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public List<ProductListDetail> getProducts(final ProductSortType sortType) {
        return productQueryRepository.getProducts(sortType);
    }

    @Override
    public List<Product> findByIdIn(final List<Long> productIdList) {
        return productJpaRepository.findByIdIn(productIdList);
    }
}
