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
        IdService idService = new IdService();
        shopService = new ShopService(productRepo, orderRepo, idService);
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

    @Test
    void getOldestOrderPerStatusTest_when6Orders_then3ElementMap() {
        //GIVEN
        List<String> productsIds = List.of("1");
        Order order1older = shopService.addOrder(productsIds);
        Order order2older = shopService.addOrder(productsIds);
        Order order3older = shopService.addOrder(productsIds);
        shopService.addOrder(productsIds);
        Order order2 = shopService.addOrder(productsIds);
        Order order3 = shopService.addOrder(productsIds);


        order2older = shopService.updateOrder(order2older.id(), Order.OrderStatus.IN_DELIVERY);
        shopService.updateOrder(order2.id(), Order.OrderStatus.IN_DELIVERY);
        order3older = shopService.updateOrder(order3older.id(), Order.OrderStatus.COMPLETED);
        shopService.updateOrder(order3.id(), Order.OrderStatus.COMPLETED);

        Map<Order.OrderStatus, Order> expected = new HashMap<>();
        expected.put(Order.OrderStatus.PROCESSING, order1older);
        expected.put(Order.OrderStatus.IN_DELIVERY, order2older);
        expected.put(Order.OrderStatus.COMPLETED, order3older);


        //WHEN
        Map<Order.OrderStatus, Order> actual = shopService.getOldestOrderPerStatus();

        //THEN
        assertEquals(expected, actual);

    }
    @Test
    void getOldestOrderPerStatusTest_when4Orders_then2ElementMap() {
        //GIVEN
        List<String> productsIds = List.of("1");
        Order order1older = shopService.addOrder(productsIds);
        Order order2older = shopService.addOrder(productsIds);
        shopService.addOrder(productsIds);
        Order order2 = shopService.addOrder(productsIds);


        order2older = shopService.updateOrder(order2older.id(), Order.OrderStatus.IN_DELIVERY);
        shopService.updateOrder(order2.id(), Order.OrderStatus.IN_DELIVERY);

        Map<Order.OrderStatus, Order> expected = new HashMap<>();
        expected.put(Order.OrderStatus.PROCESSING, order1older);
        expected.put(Order.OrderStatus.IN_DELIVERY, order2older);


        //WHEN
        Map<Order.OrderStatus, Order> actual = shopService.getOldestOrderPerStatus();

        //THEN
        assertEquals(expected, actual);
    }
    @Test
    void getOldestOrderPerStatusTest_whenNoOrders_thenEmptyMap() {
        //GIVEN
        Map<Order.OrderStatus, Order> expected = new HashMap<>();

        //WHEN
        Map<Order.OrderStatus, Order> actual = shopService.getOldestOrderPerStatus();

        //THEN
        assertEquals(expected, actual);
    }
}
