package stellarburgers.testOrder;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.util.List;
import static stellarburgers.usefulData.Steps.*;
import static stellarburgers.usefulData.UsefulData.*;
import static stellarburgers.usefulData.UsefulData.generateRandomFirstName;

public class UserOrdersTest {

    private String login;
    private String password;
    private String firstName;
    static String[] ingredients;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;

        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
    }

    @Test
    @DisplayName("Получение списка заказов, авторизованный пользователь")
    public void getListOrdersAuthorizedUser() {
        createUser(login, password, firstName);
        List<String> validIngredients = getListValidIngredients();
        ingredients = new String[]{validIngredients.get(0), validIngredients.get(1)};
        createNewOrder(ingredients, accessToken);
        Response response = getListUserOrders(accessToken);
        validateResponseOk(response, 200, true);
    }

    @Test
    @DisplayName("Получение списка заказов, без авторизации")
    public void getListOrdersUnauthorizedUser() {
        Response response = getListUserOrders("");
        validateResponseError(response, 401, "You should be authorised");
    }

    @After
    public void tearDown() {
        deleteUser(accessToken);
    }

}
