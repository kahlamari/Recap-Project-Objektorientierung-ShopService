import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {
    private ShopService shopService;

    @BeforeEach
    void buildUp() {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderMapRepo();
        shopService = new ShopService(productRepo, orderRepo);
    }

    @Test
    void addOrderTest() {
        //GIVEN
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), Instant.now(), Order.OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        List<String> productsIds = List.of("1", "2");

        assertThrows(
                //THEN
                NoSuchProductException.class,

                //WHEN
                ()->shopService.addOrder(productsIds));
    }

    @Test
    void searchByStatusTest_whenOrdersWithStatusPresent_thenReturnOrders() {
        //GIVEN
        List<String> productsIds = List.of("1");
        Set<Order> expected = new HashSet<>();
        expected.add(shopService.addOrder(productsIds));
        expected.add(shopService.addOrder(productsIds));
        expected.add(shopService.addOrder(productsIds));


        //WHEN
        List<Order> actual = shopService.searchByOrderStatus(Order.OrderStatus.PROCESSING);
        Set<Order> actualSet = new HashSet<>(actual);

        //THEN
        assertEquals(actualSet, expected);
    }

    @Test
    void searchByStatusTest_whenOrdersWithStatusNotPresent_thenReturnEmptyList() {
        //GIVEN

        //WHEN
        List<Order> actual = shopService.searchByOrderStatus(Order.OrderStatus.PROCESSING);

        //THEN
        assertEquals(List.of(), actual);
    }

    @Test
    void updateOrderTest_whenNewOrderStatus_thenNewOrderStatus() {
        //GIVEN
        List<String> productsIds = List.of("1");
        Order order = shopService.addOrder(productsIds);
        Order expected = new Order(order.id(), order.products(), order.orderTimestamp(), Order.OrderStatus.IN_DELIVERY);

        //WHEN
        Order actual = shopService.updateOrder(order.id(), Order.OrderStatus.IN_DELIVERY);

        //THEN
        assertEquals(actual, expected);
    }
}
