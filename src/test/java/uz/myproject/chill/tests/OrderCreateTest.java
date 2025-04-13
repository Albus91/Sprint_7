package uz.myproject.chill.tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import uz.myproject.chill.models.OrderCreate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
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
        OrderCreate order = orderCreateData(color);
        createOrder(order);
        verifyTrackFieldExists(order);
    }

    @Step("Создание объекта заказа с цветом: {0}")
    public OrderCreate orderCreateData(List<String> color) {
        return new OrderCreate(
                "lesli",
                "dumbldedore",
                "hogwarts",
                9,
                "+1 100 100 10 10",
                15,
                "2025-06-06",
                "alohomora",
                color
        );
    }

    @Step("Отправка запроса на создание заказа")
    public void createOrder(OrderCreate order) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Проверка, что в ответе есть поле track")
    public void verifyTrackFieldExists(OrderCreate order) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .body("track", notNullValue());
    }
}