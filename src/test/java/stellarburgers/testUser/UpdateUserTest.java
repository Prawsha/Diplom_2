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

public class UpdateUserTest {

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
    @DisplayName("Изменение логина авторизованного пользователя")
    public void changeLoginAuthorizedUser() {
        createUser(login, password, firstName);
        Response response = updateUser("new"+login, password, firstName, accessToken);
        validateResponseOk(response, 200, true);
    }

    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    public void changePasswordAuthorizedUser() {
        createUser(login, password, firstName);
        Response response = updateUser(login, "new"+password, firstName, accessToken);
        validateResponseOk(response, 200, true);
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    public void changeFirstNameAuthorizedUser() {
        createUser(login, password, firstName);
        Response response = updateUser(login, password, "new"+firstName, accessToken);
        validateResponseOk(response, 200, true);
    }

    @Test
    @DisplayName("Изменение логина пользователя без авторизации")
    public void changeLoginUnauthorizedUser() {
        createUser(login, password, firstName);
        Response response = updateUser("new"+login, password, firstName, "");
        validateResponseError(response, 401, "You should be authorised");
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    public void changePasswordUnauthorizedUser() {
        createUser(login, password, firstName);
        Response response = updateUser(login, "new"+password, firstName, "");
        validateResponseError(response, 401, "You should be authorised");
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    public void changeFirstNameUnauthorizedUser() {
        createUser(login, password, firstName);
        Response response = updateUser(login, password, "new"+firstName, "");
        validateResponseError(response, 401, "You should be authorised");
    }

    @After
    public void tearDown() {
        deleteUser(accessToken);
    }

}
