import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ShopService {
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final IdService idService;

    public Order addOrder(List<String> productIds) {
        return addOrderWithId(idService.generateId(), productIds);
    }

    public Order addOrderWithId(String id, List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Product productToOrder = productRepo.getProductById(productId)
                    .orElseThrow(() -> new NoSuchProductException("Produkt mit der Id: " + productId + " konnte nicht bestellt werden!"));

            BigDecimal quantity = productRepo.getProductById(productId).get().quantity();

            if (quantity.intValue() == 0) {
                throw new NoSuchProductException("Produkt mit der Id: " + productId + " ist nicht mehr vorrätig!");
            }
            Product productNewQuantity = productRepo.getProductById(productId).get().withQuantity(quantity.subtract(new BigDecimal("1")));
            productRepo.removeProduct(productId);
            productRepo.addProduct(productNewQuantity);
            products.add(productToOrder);
        }

        Order newOrder = new Order(id, products, Instant.now(), Order.OrderStatus.PROCESSING);

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

    public String printOrdersToString() {
        return orderRepo.toString();
    }
}
