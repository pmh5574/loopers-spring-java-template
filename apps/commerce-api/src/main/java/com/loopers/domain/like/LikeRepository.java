package com.loopers.domain.like;

public interface LikeRepository {
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    int saveIgnore(final Long userId, final Long productId);
}
