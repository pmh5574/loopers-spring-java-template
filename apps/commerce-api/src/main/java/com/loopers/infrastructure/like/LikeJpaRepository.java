package com.loopers.infrastructure.like;

import com.loopers.domain.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeJpaRepository extends JpaRepository<Like, Long> {
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    @Modifying
    @Query(
            value = "INSERT IGNORE INTO likes (user_id, product_id, created_at, updated_at) " +
                    "VALUES (:userId, :productId, NOW(), NOW()) ",
            nativeQuery = true
    )
    int saveIgnore(@Param("userId") Long userId, @Param("productId") Long productId);
}
