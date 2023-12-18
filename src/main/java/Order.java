import java.util.List;

public record Order(
        String id,
        List<Product> products,
        OrderStatus orderStatus
) {
    public enum OrderStatus {
        PROCESSING, IN_DELIVERY, COMPLETED
    }
}
