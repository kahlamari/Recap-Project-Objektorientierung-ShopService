import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), Order.OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService();
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
        ShopService shopService = new ShopService();
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
    void serachByStatusTest_whenOrdersWithStatusNotPresent_thenReturnEmptyList() {
        //GIVEN
        ShopService shopService = new ShopService();

        //WHEN
        List<Order> actual = shopService.searchByOrderStatus(Order.OrderStatus.PROCESSING);

        //THEN
        assertEquals(List.of(), actual);
    }
}
