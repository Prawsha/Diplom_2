package stellarburgers.usefulData;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static stellarburgers.usefulData.UsefulData.*;

public class Steps {

    public static String accessToken;
    public static Order order;
    public static User user;

    public static void setAccessToken(String accessToken) {
        Steps.accessToken = accessToken;
    }

    @Step("Создание пользователя")
    public static Response createUser(String email, String password, String name) {
        user = new User(email, password, name);
        Response response =  given()
                .header("Content-type", "application/json")
                .body(user)
                .post(USER_REGISTER);
        accessToken = response.then()
                .extract().path("accessToken");
        System.out.println("Created user with accessToken: " + accessToken);
        return response;
    }

    @Step("Логин пользователя")
    public static Response loginUser(String email, String password) {
        user = new User(email, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(user)
                .post(USER_LOGIN);
        return response;
    }

    @Step("Обновление данных пользователя")
    public static Response updateUser(String email, String password, String name, String accessToken) {
        user = new User(email, password, name);
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .patch(USER_UPDATE_DELETE);
        return response;
    }

    @Step("Удаление пользователя")
    public static void deleteUser(String accessToken) {
        if (accessToken != null) {
            given()
                    .header("Content-type", "application/json")
                    .header("Authorization", accessToken)
                    .delete(USER_UPDATE_DELETE);
            System.out.println("Deleted user with accessToken: " + accessToken);
        }
    }

    @Step("Ответ об успешном создании/логине/изменения юзера или создании заказа")
    public static void validateResponseOk(Response response, int expectedStatusCode, Boolean expectedMessage) {
        response.then()
                .statusCode(expectedStatusCode)
                .body("success", equalTo(expectedMessage));
    }

    @Step("Ответ об ошибке при создании/логине/изменения юзера или создании заказа")
    public static void validateResponseError(Response response, int expectedStatusCode, String expectedMessage) {
        response.then()
                .statusCode(expectedStatusCode)
                .body("message", equalTo(expectedMessage));
    }

    @Step("Проверка ответа Error 500")
    public static void validateResponseError500(Response response, int expectedStatusCode) {
        response.then()
                .statusCode(expectedStatusCode);
    }

    @Step("Создание заказа")
    public static Response createNewOrder(String[] ingredients, String accessToken) {
        order = new Order(ingredients);
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .post(ORDER_CREATE_GET);
        return response;
    }

    @Step("Получение списка ингредиентов")
    public static List<String> getListValidIngredients() {
        Response response = given()
                .get(INGREDIENTS_LIST);
        return response.jsonPath().getList("data._id");
    }

    @Step("Получение списка заказов пользователя")
    public static Response getListUserOrders(String accessToken) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .get(ORDER_CREATE_GET);
        return response;
    }

}