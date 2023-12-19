import lombok.With;

import java.math.BigDecimal;

public record Product(
        String id,
        String name,
        @With
        BigDecimal quantity
) {
}
