package uz.myproject.chill.clients;

import io.qameta.allure.Step;
import uz.myproject.chill.models.OrderCreate;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static uz.myproject.chill.constants.Endpoints.*;

public class OrderClient {

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
                .post(ORDERS);
    }

    @Step("Проверка, что в ответе есть поле track")
    public void verifyTrackFieldExists(OrderCreate order) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(ORDERS)
                .then()
                .body("track", notNullValue());
    }

    @Step("Отправка запроса на список заказов")
    public void sendOrdersRequest() {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .when()
                .get(ORDERS)
                .then()
                .extract()
                .asString();
    }

    @Step("Проверка - статус код ответа 200")
    public void verifyResponseStatusCode() {
        given()
                .baseUri(BASE_URL)
                .when()
                .get(ORDERS)
                .then()
                .statusCode(200);
    }

    @Step("Проверка наличия непустого списка в теле ответа")
    public void verifyResponseBody() {
        given()
                .baseUri(BASE_URL)
                .when()
                .get(ORDERS)
                .then()
                .body("orders.size()", greaterThan(0));
    }
}
