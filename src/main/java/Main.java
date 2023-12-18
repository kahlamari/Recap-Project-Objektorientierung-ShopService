import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderMapRepo();
        ShopService shopService = new ShopService(productRepo, orderRepo);

        Product pear = new Product("2", "Pear");
        productRepo.addProduct(pear);
        Product banana = new Product("3", "Banana");
        productRepo.addProduct(banana);

        shopService.addOrder(List.of("1","2"));
        shopService.addOrder(List.of("1","3"));
        shopService.addOrder(List.of("2","3"));

        System.out.println(orderRepo.getOrders());
    }
}
