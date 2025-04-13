package uz.myproject.chill.tests;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uz.myproject.chill.models.CourierCreate;
import uz.myproject.chill.models.CourierLogin;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTest {

    private static final String BASE_URL = "http://qa-scooter.praktikum-services.ru";
    private static String courierId;

    @Before
    public void courierCreate() {
        CourierCreate courierCreate = new CourierCreate("courier.login", "courier.password", "courier.name");
        createNewCourier(courierCreate);
    }

    @After
    public void cleanup() {
        CourierLogin courierLogin = new CourierLogin("courier.login", "courier.password");
        courierId = loginCourier(courierLogin);
        deleteCourier(courierId);
    }

    @Test
    @DisplayName("Успешный логин за курьера")
    public void testCourierLoginSuccess() {
        CourierLogin courierLogin = new CourierLogin("courier.login", "courier.password");
        loginCourierSuccess(courierLogin);
    }

    @Test
    @DisplayName("Невозможно зайти за курьера без указания логина")
    public void testCourierLoginWithoutLoginFails() {
        CourierLogin courierLogin = new CourierLogin(null, "courier.password");
        loginFails(courierLogin, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Невозможно зайти за курьера без указания пароля")
    public void testCourierLoginWithoutPasswordFails() {
        CourierLogin courierLogin = new CourierLogin("courier.login", null);
        loginFails(courierLogin, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Невозможно зайти за курьера с неверным логином")
    public void testCourierLoginWithWrongLoginFails() {
        CourierLogin courierLogin = new CourierLogin("courier.wronglogin", "courier.password");
        loginFails(courierLogin, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Невозможно зайти за курьера с неверным паролем")
    public void testCourierLoginWithWrongPasswordFails() {
        CourierLogin courierLogin = new CourierLogin("courier.login", "courier.wrongpassword");
        loginFails(courierLogin, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Невозможно зайти за курьера с неверными данными")
    public void testCourierLoginWithNonExistingCredentialsFails() {
        CourierLogin courierLogin = new CourierLogin("avadakedavra", "volandemort");
        loginFails(courierLogin, 404, "Учетная запись не найдена");
    }

    @Step("Создание курьера")
    public void createNewCourier(CourierCreate courierCreate) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courierCreate)
                .when()
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Логин за курьера: {courierLogin.login}")
    public String loginCourier(CourierLogin courierLogin) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .extract().jsonPath().getString("id");
    }

    @Step("Удаление курьера: {courierId}")
    public void deleteCourier(String courierId) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .when()
                .delete("/api/v1/courier/" + courierId);
    }

    @Step("Успешный логин за курьера: {courierLogin.login}")
    public void loginCourierSuccess(CourierLogin courierLogin) {
        courierId = given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().jsonPath().getString("id");
    }

    @Step("Невозможно залогиниться, имя: {courierLogin.login}, пароль: {courierLogin.password}")
    public void loginFails(CourierLogin courierLogin, int expectedStatusCode, String expectedMessage) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }
}
