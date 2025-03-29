package stellarburgers.usefulData;

import java.util.Random;

public class UsefulData {

    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";

    public static final String USER_REGISTER = "/api/auth/register";
    public static final String USER_LOGIN = "/api/auth/login";
    public static final String USER_UPDATE_DELETE = "/api/auth/user";
    public static final String INGREDIENTS_LIST = "/api/ingredients";
    public static final String ORDER_CREATE_GET = "/api/orders";

    // Рандомные тестовые данные
    private static final Random random = new Random();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    // Рандомный логин
    public static String generateRandomLogin() {
        return generateRandomString(6) + "@yandex.ru";
    }
    // Рандомный пароль
    public static String generateRandomPassword() {
        return generateRandomString(6);
    }
    // Рандомное имя пользователя
    public static String generateRandomFirstName() {
        return "FirstName" + generateRandomString(6);
    }
    // Рандомная стринга
    private static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}