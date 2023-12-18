import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new NoSuchProductException("Produkt mit der Id: " + productId + " konnte nicht bestellt werden!");
            }
            products.add(productToOrder.get());
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, Instant.now(), Order.OrderStatus.PROCESSING);

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
}
