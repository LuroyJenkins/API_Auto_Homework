package Tests;

import org.junit.jupiter.api.*;
import org.json.JSONObject;

import java.io.IOException;

import static Helpers.Utils.readFile;
import static io.restassured.RestAssured.given;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Store extends Base {

    String requestBody = null;
    int petId = 2811;
    int quantity = 1;
    String status = "placed";
    boolean complete = true;

    {
        //Получаем тело запроса
        String initialBody = null;
        try {
            initialBody = readFile("src/test/resources/placeOrder.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Изменяем тело запроса
        JSONObject jsonObject = new JSONObject(initialBody);
        jsonObject.put("petId", petId);
        jsonObject.put("status", status);
        jsonObject.put("quantity", quantity);
        requestBody = jsonObject.toString();
    }

    // Тесты для метода POST /store/order
    @Test
    @DisplayName("POSITIVE.Размещение заказа на питомца")
    @Order(1)
    public void test1() throws IOException {
        given(placeOrderForPet(requestBody))
                .post()
                .then()
                .spec(placeOrderForPetResp(2811, "placed", 1));
    }

    @Test
    @DisplayName("NEGATIVE.Размещение заказа на питомца. Отправка STRING в boolean параметр JSON")
    @Order(2)
    public void test2() throws IOException {
        //Изменяем тело запроса
        JSONObject jsonObject = new JSONObject(requestBody);
        jsonObject.put("id", "Kate");

        given(placeOrderForPet(requestBody))
                .post()
                .then()
                .spec(getAssertionSpec(400)); // он работает, у них валидация на уровне фронта только
    }

    @Test
    @DisplayName("NEGATIVE.Размещение заказа на питомца. Отправка пустого JSON.")
    @Order(3)
    public void test3() throws IOException {
        given(placeOrderForPet(""))
                .post()
                .then()
                .spec(getAssertionSpec(400));
    }

    // Тесты для метода GET /store/order/{orderID}

    @Test
    @DisplayName("POSITIVE. Поиск заказа по ID.")
    @Order(4)
    public void test4() throws IOException {
        given(findOrderByID("4"))
                .get()
                .then()
                .spec(placeOrderForPetResp(2811, "placed", 1));
    }

    @Test
    @DisplayName("NEGATIVE. Поиск несуществующего заказа по ID.")
    @Order(5)
    public void test5() throws IOException {
        given(findOrderByID("8"))
                .get()
                .then()
                .spec(getAssertionSpec(404));
    }

    @Test
    @DisplayName("NEGATIVE. Поиск несуществующего заказа по ID.")
    @Order(6)
    public void test6() throws IOException {
        given(findOrderByID("8"))
                .get()
                .then()
                .spec(orderForPetResp(404, "error", "Order not found"));
    }

    // Тесты для метода DELETE /store/order/{orderID}

    @Test
    @DisplayName("POSITIVE. Удаление заказа по ID.")
    @Order(7)
    public void test7() throws IOException {
        given(findOrderByID("4"))
                .delete()
                .then()
                .spec(orderForPetResp(200, "unknown", "4"));
    }

    @Test
    @DisplayName("NEGATIVE. Удаление несуществующего заказа по ID.")
    @Order(8)
    public void test8() throws IOException {
        given(findOrderByID("123"))
                .delete()
                .then()
                .spec(orderForPetResp(404, "unknown", "Order Not Found"));
    }

    @Test
    @DisplayName("NEGATIVE. Удаление. Передача некорректного параметра в URL")
    @Order(9)
    public void test9() throws IOException {
        given(findOrderByID("asd"))
                .delete()
                .then()
                .spec(orderForPetResp(404, "unknown", "java.lang.NumberFormatException: For input string: \"asd\""));
    }

    // Тесты для метода GET /store/inventory
    @Test
    @DisplayName("POSITIVE. Получение списка питомцев по статусам")
    @Order(10)
    public void test10() throws IOException {
        given(returnsPetInventories())
                .get()
                .then()
                .spec(returnsPetInventoriesResp());
    }
}
