package uz.myproject.chill.tests;

import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import uz.myproject.chill.clients.OrderClient;

public class OrdersListTest {

    private final OrderClient client = new OrderClient();

    @Test
    @DisplayName("Проверка вызова списка заказов")
    public void testOrdersListReturnsOrders() {
        client.sendOrdersRequest();
        client.verifyResponseStatusCode();
        client.verifyResponseBody();
    }
}
