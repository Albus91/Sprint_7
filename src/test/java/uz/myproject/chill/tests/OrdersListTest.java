package uz.myproject.chill.tests;

import io.qameta.allure.Step;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListTest {

    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";

    @Test
    @DisplayName("Проверка вызова списка заказов")
    public void testOrdersListReturnsOrders() {
        sendOrdersRequest();
        verifyResponseStatusCode();
        verifyResponseBody();
    }

    @Step("Отправка запроса на список заказов")
    public void sendOrdersRequest() {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then()
                .extract()
                .asString();
    }

    @Step("Проверка - статус код ответа 200")
    public void verifyResponseStatusCode() {
        given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/v1/orders")
                .then()
                .statusCode(200);
    }

    @Step("Проверка наличия поля orders в теле ответа")
    public void verifyResponseBody() {
        given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/v1/orders")
                .then()
                .body("orders", notNullValue());
    }
}
