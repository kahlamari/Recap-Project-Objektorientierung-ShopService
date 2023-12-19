import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderMapRepoTest {

    @Test
    void getOrders() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();
        Instant orderTimestamp = Instant.now();
        Product product = new Product("1", "Apfel", new BigDecimal("1"));
        Order newOrder = new Order("1", List.of(product), orderTimestamp, Order.OrderStatus.PROCESSING);
        repo.addOrder(newOrder);

        //WHEN
        List<Order> actual = repo.getOrders();

        //THEN
        List<Order> expected = new ArrayList<>();
        Product product1 = new Product("1", "Apfel", new BigDecimal("1"));
        expected.add(new Order("1", List.of(product1), orderTimestamp, Order.OrderStatus.PROCESSING));

        assertEquals(actual, expected);
    }

    @Test
    void getOrderById() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();
        Instant orderTimestamp = Instant.now();
        Product product = new Product("1", "Apfel", new BigDecimal("1"));
        Order newOrder = new Order("1", List.of(product), orderTimestamp, Order.OrderStatus.PROCESSING);
        repo.addOrder(newOrder);

        //WHEN
        Order actual = repo.getOrderById("1");

        //THEN
        Product product1 = new Product("1", "Apfel", new BigDecimal("1"));
        Order expected = new Order("1", List.of(product1), orderTimestamp, Order.OrderStatus.PROCESSING);

        assertEquals(actual, expected);
    }

    @Test
    void addOrder() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();
        Product product = new Product("1", "Apfel", new BigDecimal("10"));
        Instant orderTimestamp = Instant.now();
        Order newOrder = new Order("1", List.of(product), orderTimestamp, Order.OrderStatus.PROCESSING);

        //WHEN
        Order actual = repo.addOrder(newOrder);

        //THEN
        Product product1 = new Product("1", "Apfel", new BigDecimal("10"));
        Order expected = new Order("1", List.of(product1), orderTimestamp, Order.OrderStatus.PROCESSING);
        assertEquals(actual, expected);
        assertEquals(repo.getOrderById("1"), expected);
    }

    @Test
    void removeOrder() {
        //GIVEN
        OrderMapRepo repo = new OrderMapRepo();

        //WHEN
        repo.removeOrder("1");

        //THEN
        assertNull(repo.getOrderById("1"));
    }
}
