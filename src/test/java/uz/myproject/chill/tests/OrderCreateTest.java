package uz.myproject.chill.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uz.myproject.chill.models.OrderCreate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import uz.myproject.chill.clients.OrderClient;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private final OrderClient client = new OrderClient();
    private final List<String> color;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Order with color: {0}")
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()}
        });
    }

    @Test
    @DisplayName("Проверка создания заказа с разными вариантами цветов")
    public void testOrderCreateWithDifferentColorVariations() {
        OrderCreate order = client.orderCreateData(color);
        client.createOrder(order);
        client.verifyTrackFieldExists(order);
    }
}