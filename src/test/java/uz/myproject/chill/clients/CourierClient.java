package uz.myproject.chill.clients;

import io.qameta.allure.Step;
import uz.myproject.chill.models.CourierCreate;
import uz.myproject.chill.models.CourierLogin;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static uz.myproject.chill.constants.Endpoints.*;

public class CourierClient {

    @Step("Создание курьера")
    public void createCourier(CourierCreate courier) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER);
    }

    @Step("Создание курьера с ошибкой. Ожидаемый статус: {1}, сообщение: {2}")
    public void createCourierFails(CourierCreate courier, int expectedStatus, String expectedMessage) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER)
                .then()
                .statusCode(expectedStatus)
                .body("message", equalTo(expectedMessage));
    }

    @Step("Логин за курьера")
    public String loginCourier(CourierLogin courierLogin) {
        return given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courierLogin)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .extract().jsonPath().getString("id");
    }

    @Step("Удаление курьера по id")
    public void deleteCourier(String courierId) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .when()
                .delete(DELETE_COURIER + courierId)
                .then()
                .statusCode(200);
    }

    @Step("Проверка, что курьер успешно создан")
    public void checkCourierCreatedSuccessfully(CourierCreate courier) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courier)
                .when()
                .post(CREATE_COURIER)
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
                .post(CREATE_COURIER)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Логин за курьера и удаление по id")
    public void loginCourierAndDeleteCourier(String login, String password) {
        CourierLogin courierLogin = new CourierLogin(login, password);
        String courierId = given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courierLogin)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .extract().jsonPath().getString("id");
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .when()
                .delete(DELETE_COURIER + courierId);
    }

    @Step("Успешный логин за курьера")
    public void loginCourierSuccess(CourierLogin courierLogin) {
        String courierId = given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courierLogin)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .extract().jsonPath().getString("id");
    }

    @Step("Невозможно залогиниться")
    public void loginFails(CourierLogin courierLogin, int expectedStatusCode, String expectedMessage) {
        given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .body(courierLogin)
                .when()
                .post(LOGIN_COURIER)
                .then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }
}
