package uz.myproject.chill.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import uz.myproject.chill.models.CourierCreate;
import uz.myproject.chill.clients.CourierClient;

public class CourierCreateTest {

    CourierClient client = new CourierClient();

    @After
    public void cleanup() {
        client.loginCourierAndDeleteCourier("courier.login", "courier.password");
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCourierCreateSuccess() {
        CourierCreate courier = new CourierCreate("courier.login", "courier.password", "courier.name");
        client.checkCourierCreatedSuccessfully(courier);
    }

    @Test
    @DisplayName("Невозможно создать курьера с уже существующим логином")
    public void testCreateCourierWithExistingLoginFails() {
        CourierCreate courier = new CourierCreate("courier.login", "courier.password", "courier.name");
        client.createCourier(courier);
        client.createCourierWithExistingLogin(courier);
    }

    @Test
    @DisplayName("Невозможно создать курьера без указания логина")
    public void testCreateCourierWithMissingLoginFails() {
        CourierCreate courier = new CourierCreate(null, "courier.password", "courier.name");
        client.createCourierFails(courier, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Невозможно создать курьера без указания пароля")
    public void testCreateCourierWithMissingPasswordFails() {
        CourierCreate courier = new CourierCreate("courier.login", null, "courier.name");
        client.createCourierFails(courier, 400, "Недостаточно данных для создания учетной записи");
    }
}
