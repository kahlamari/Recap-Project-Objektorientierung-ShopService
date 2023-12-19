import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final IdService idService;

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new NoSuchProductException("Produkt mit der Id: " + productId + " konnte nicht bestellt werden!");
            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(idService.generateId(), products, Instant.now(), Order.OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }

    public List<Order> searchByOrderStatus(Order.OrderStatus orderStatus) {
        return orderRepo.getOrders().stream().filter(o -> o.orderStatus().equals(orderStatus)).collect(Collectors.toList());
    }

    public Order updateOrder(String id, Order.OrderStatus orderStatus) {
        Order updatedOrder = orderRepo.getOrderById(id).withOrderStatus(orderStatus);
        orderRepo.removeOrder(id);
        orderRepo.addOrder(updatedOrder);
        return updatedOrder;
    }

    public Map<Order.OrderStatus,Order> getOldestOrderPerStatus() {
        Map<Order.OrderStatus, Order> orderStatusOrderMap = new HashMap<>();

        for (Order.OrderStatus orderStatus : Order.OrderStatus.values()) {
            searchByOrderStatus(orderStatus).stream()
                    .min(Comparator.comparing(Order::orderTimestamp))
                    .ifPresent(order -> orderStatusOrderMap.put(orderStatus, order));
        }
        return orderStatusOrderMap;
    }
}
