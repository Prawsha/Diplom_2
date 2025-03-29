package stellarburgers.testUser;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static stellarburgers.usefulData.Steps.*;
import static stellarburgers.usefulData.UsefulData.*;
import static stellarburgers.usefulData.UsefulData.generateRandomFirstName;

public class LoginUserTest {

    private String login;
    private String password;
    private String firstName;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void successLogin() {
        createUser(login, password, firstName);
        Response response = loginUser(login, password);
        validateResponseOk(response, 200, true);
    }

    @Test
    @DisplayName("Логин с неверным логином")
    public void loginWithWrongLogin() {
        createUser(login, password, firstName);
        Response response = loginUser(login+"fake", password);
        validateResponseError(response, 401, "email or password are incorrect");
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    public void loginWithWrongPassword() {
        createUser(login, password, firstName);
        Response response = loginUser(login, password+"fake");
        validateResponseError(response, 401, "email or password are incorrect");
    }

    @After
    public void tearDown() {
        deleteUser(accessToken);
    }
}
