package com.loopers.domain.product;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandService;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandService brandService;

    public ProductDetail getProductDetail(Long productId) {
        Product product = getProduct(productId);
        Brand brand = brandService.getBrand(product.getBrandId());
        return new ProductDetail(product, brand);
    }

    public ProductList getProducts(ProductSortType sortType) {
        List<ProductListDetail> productListDetails = productRepository.getProducts(sortType);
        return new ProductList(productListDetails);
    }

    public Product getProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "[id = " + productId + "] 상품을 찾을 수 없습니다."));
    }

    @Transactional
    public void likeCountIncrease(final Long productId, final int amount) {
        getProduct(productId).likeCountIncrease(amount);
    }
}
