package uz.myproject.chill.tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import uz.myproject.chill.models.CourierCreate;
import uz.myproject.chill.models.CourierLogin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourierCreateTest {

    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    private static String courierId;

    @After
    public void cleanup() {
        loginCourierAndDeleteCourier("courier.login", "courier.password");
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCourierCreateSuccess() {
        CourierCreate courier = new CourierCreate("courier.login", "courier.password", "courier.name");
        checkCourierCreatedSuccessfully(courier);
    }

    @Test
    @DisplayName("Невозможно создать курьера с уже существующим логином")
    public void testCreateCourierWithExistingLoginFails() {
        CourierCreate courier = new CourierCreate("courier.login", "courier.password", "courier.name");
        createCourier(courier);
        createCourierWithExistingLogin(courier);
    }

    @Test
    @DisplayName("Невозможно создать курьера без указания логина")
    public void testCreateCourierWithMissingLoginFails() {
        CourierCreate courier = new CourierCreate(null, "courier.password", "courier.name");
        createCourierFails(courier, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Невозможно создать курьера без указания пароля")
    public void testCreateCourierWithMissingPasswordFails() {
        CourierCreate courier = new CourierCreate("courier.login", null, "courier.name");
        createCourierFails(courier, 400, "Недостаточно данных для создания учетной записи");
    }

    // Шаги

    @Step("Создание курьера с ошибкой. Ожидаемый статус: {1}, сообщение: {2}")
    public void createCourierFails(CourierCreate courier, int expectedStatus, String expectedMessage) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(expectedStatus)
                .body("message", equalTo(expectedMessage));
    }

    @Step("Создание курьера: {0}")
    public void createCourier(CourierCreate courier) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Проверка, что курьер успешно создан")
    public void checkCourierCreatedSuccessfully(CourierCreate courier) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Создание курьера с уже существующим логином")
    public void createCourierWithExistingLogin(CourierCreate courier) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Логин за курьера {0} и удаление по id")
    public void loginCourierAndDeleteCourier(String login, String password) {
        CourierLogin courierLogin = new CourierLogin(login, password);
        courierId = given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().jsonPath().getString("id");
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .when()
                .delete("/api/v1/courier/" + courierId);
    }
}
