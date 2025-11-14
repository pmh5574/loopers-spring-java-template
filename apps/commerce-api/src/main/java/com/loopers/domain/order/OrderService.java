package com.loopers.domain.order;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointService;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductService;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final PointService pointService;

    @Transactional
    public Order createOrder(Long userId, List<OrderCreateItem> orderCreateItems) {
        User user = userService.getUser(userId);
        Point point = pointService.getPointByUserModelId(userId);
        Order order = Order.create(userId);

        List<Long> productIdList = orderCreateItems.stream()
                .map(OrderCreateItem::productId)
                .toList();

        List<Product> products = productService.getProductIn(productIdList);

        Map<Long, Integer> itemQuantityMap = orderCreateItems.stream()
                .collect(Collectors.toMap(OrderCreateItem::productId, OrderCreateItem::quantity));

        List<OrderItem> orderItems = products.stream()
                .map(product -> {
                    int quantity = itemQuantityMap.get(product.getId());
                    product.getStock().decrease(quantity);
                    return OrderItem.create(quantity, product.getPrice(), product.getId(), order.getId());
                }).toList();

        Long totalPrice = orderItems.stream()
                .mapToLong(OrderItem::getProductPrice)
                .sum();
        point.usePoint(totalPrice);
        return orderRepository.save(order, orderItems);
    }
}
