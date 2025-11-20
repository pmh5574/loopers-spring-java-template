package com.loopers.infrastructure.product;

import com.loopers.domain.product.Product;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       SELECT p 
       FROM Product p 
       WHERE p.id IN :ids 
       ORDER BY p.id 
       """)
    List<Product> findAllByIdInWithLock(@Param("ids") List<Long> ids);
}
