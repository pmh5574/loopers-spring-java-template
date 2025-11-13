package com.loopers.domain.product;

public record ProductListDetail(
        Long id,
        String name,
        Integer price,
        long stock,
        int likeCount,
        Long brandId,
        String brandName
) {
}
