import lombok.With;

import java.time.Instant;
import java.util.List;

public record Order(
        String id,
        List<Product> products,
        Instant orderTimestamp,
        @With
        OrderStatus orderStatus
) {
    public enum OrderStatus {
        PROCESSING, IN_DELIVERY, COMPLETED
    }
}
