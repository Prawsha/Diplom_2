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

public class OrderCreationTest {

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
    @DisplayName("Создание заказа с ингредиентами, авторизованный пользователь")
    public void createOrderWithIngredientsAuthorizedUser() {
        createUser(login, password, firstName);
        List<String> validIngredients = getListValidIngredients();
        ingredients = new String[]{validIngredients.get(0), validIngredients.get(1)};
        Response response = createNewOrder(ingredients, accessToken);
        validateResponseOk(response, 200, true);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, авторизованный пользователь")
    public void createOrderWithoutIngredientsAuthorizedUser() {
        createUser(login, password, firstName);
        ingredients = new String[]{};
        Response response = createNewOrder(ingredients, accessToken);
        validateResponseError(response, 400, "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов, авторизованный пользователь")
    public void createOrderWithWrongIngredientsAuthorizedUser() {
        createUser(login, password, firstName);
        ingredients = new String[]{"wrong_ingredient", "wrong_ingredient_2"};
        Response response = createNewOrder(ingredients, accessToken);
        validateResponseError500(response, 500);
    }

    @Test
    @DisplayName("Проверка создания заказа с ингредиентами, c авторизацией после составления заказа")
    public void createOrderWithIngredientsUnauthorizedUser() {
        List<String> validIngredients = getListValidIngredients();
        ingredients = new String[]{validIngredients.get(0), validIngredients.get(1)};
        createUser(login, password, firstName);
        Response response = createNewOrder(ingredients, "");
        validateResponseOk(response, 200, true);
    }

    @After
    public void tearDown() {
        deleteUser(accessToken);
    }

}