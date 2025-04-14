package uz.myproject.chill.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uz.myproject.chill.clients.CourierClient;
import uz.myproject.chill.models.CourierCreate;
import uz.myproject.chill.models.CourierLogin;

public class CourierLoginTest {

    private final CourierClient client = new CourierClient();
    private static String courierId;

    @Before
    public void courierCreate() {
        CourierCreate courier = new CourierCreate("courier.login", "courier.password", "courier.name");
        client.createCourier(courier);
    }

    @After
    public void cleanup() {
        CourierLogin courierLogin = new CourierLogin("courier.login", "courier.password");
        courierId = client.loginCourier(courierLogin);
        client.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Успешный логин за курьера")
    public void testCourierLoginSuccess() {
        CourierLogin courierLogin = new CourierLogin("courier.login", "courier.password");
        client.loginCourierSuccess(courierLogin);
    }

    @Test
    @DisplayName("Невозможно зайти за курьера без указания логина")
    public void testCourierLoginWithoutLoginFails() {
        CourierLogin courierLogin = new CourierLogin(null, "courier.password");
        client.loginFails(courierLogin, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Невозможно зайти за курьера без указания пароля")
    public void testCourierLoginWithoutPasswordFails() {
        CourierLogin courierLogin = new CourierLogin("courier.login", null);
        client.loginFails(courierLogin, 400, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Невозможно зайти за курьера с неверным логином")
    public void testCourierLoginWithWrongLoginFails() {
        CourierLogin courierLogin = new CourierLogin("courier.wronglogin", "courier.password");
        client.loginFails(courierLogin, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Невозможно зайти за курьера с неверным паролем")
    public void testCourierLoginWithWrongPasswordFails() {
        CourierLogin courierLogin = new CourierLogin("courier.login", "courier.wrongpassword");
        client.loginFails(courierLogin, 404, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Невозможно зайти за курьера с неверными данными")
    public void testCourierLoginWithNonExistingCredentialsFails() {
        CourierLogin courierLogin = new CourierLogin("avadakedavra", "volandemort");
        client.loginFails(courierLogin, 404, "Учетная запись не найдена");
    }
}
