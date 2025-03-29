package stellarburgers.testUser;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import stellarburgers.usefulData.Steps;
import static stellarburgers.usefulData.Steps.*;
import static stellarburgers.usefulData.UsefulData.*;

public class CreationUserTest {

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
    @DisplayName("Создать уникального пользователя")
    public void createNewUser() {
        Response response = createUser(login, password, firstName);
        validateResponseOk(response, 200, true);
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    public void createTheSameUser() {
        Response response1 = createUser(login, password, firstName);
        String accessToken1 = response1.jsonPath().getString("accessToken");

        Response response2 = createUser(login, password, firstName);
        // Проверка ответа по второму юзеру - дубликату
        validateResponseError(response2, 403, "User already exists");

        setAccessToken(accessToken1);
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить поле логин")
    public void createUserWithoutLogin() {
        Response response = createUser("", password, firstName);
        validateResponseError(response, 403, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить поле пароль")
    public void createUserWithoutPassword() {
        Response response = createUser(login, "", firstName);
        validateResponseError(response, 403, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создать пользователя и не заполнить поле имя")
    public void createUserWithoutFirstName() {
        Response response = createUser(login, password, "");
        validateResponseError(response, 403, "Email, password and name are required fields");
    }

    @After
    public void tearDown() {
        Steps.deleteUser(accessToken);
    }

}
