package com.loopers.domain.like;

import com.loopers.domain.product.ProductService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class LikeService {
    private final LikeRepository likeRepository;
    private final ProductService productService;

    @Transactional
    public Like createLike(final Long userId, final Long productId) {
        if (likeRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new CoreException(ErrorType.CONFLICT, "이미 좋아요를 하셨습니다.");
        }
        Like like = Like.create(userId, productId);
        int inserted = likeRepository.saveIgnore(like.getUserId(), like.getProductId());
        if (inserted > 0) {
            productService.likeCountIncrease(productId, 1);
        }
        return like;
    }
}
