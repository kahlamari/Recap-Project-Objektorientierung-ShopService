import lombok.With;

import java.util.List;

public record Order(
        String id,
        List<Product> products,
        @With
        OrderStatus orderStatus
) {
    public enum OrderStatus {
        PROCESSING, IN_DELIVERY, COMPLETED
    }
}
