import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static List<String> commandLines;
    public static void main(String[] args) {
        ProductRepo productRepo = new ProductRepo();
        OrderRepo orderRepo = new OrderMapRepo();
        IdService idService = new IdService();
        ShopService shopService = new ShopService(productRepo, orderRepo, idService);

        Product pear = new Product("2", "Pear");
        productRepo.addProduct(pear);
        Product banana = new Product("3", "Banana");
        productRepo.addProduct(banana);
        Product orange = new Product("4", "Orange");
        productRepo.addProduct(orange);

        readTransactions();
        processTransactions(shopService);
    }

    public static void readTransactions() {
        try {
            commandLines = Files.lines(Paths.get(Main.class.getResource("/transactions.txt").toURI())).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public static void processTransactions(ShopService shopService) {
        for (String command : commandLines) {
            String[] commandSplit = command.split(" ");
            if (commandSplit[0].equals("addOrder")) {
                List<String> productIds = new ArrayList<>();
                productIds.addAll(Arrays.asList(commandSplit).subList(2, commandSplit.length));
                shopService.addOrderWithId(commandSplit[1], productIds);
            } else if (commandSplit[0].equals("setStatus")) {
                Order.OrderStatus orderStatus;
                if (commandSplit[2].equals("COMPLETED")) {
                    orderStatus = Order.OrderStatus.COMPLETED;
                } else if (commandSplit[2].equals("INDELIVERY")) {
                    orderStatus = Order.OrderStatus.IN_DELIVERY;
                } else {
                    orderStatus = Order.OrderStatus.PROCESSING;
                }
                shopService.updateOrder(commandSplit[1], orderStatus);
            } else if (commandSplit[0].equals("printOrders")) {
                System.out.println(shopService.printOrdersToString());
            }
        }
    }
}
