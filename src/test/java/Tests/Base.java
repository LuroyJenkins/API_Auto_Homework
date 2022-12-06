package Tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.*;

public class Base {

    // Возвращает базовую RequestSpecification со значениями для общих вызываемых методов

    private RequestSpecification getBaseSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json;;charset=UTF-8")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("api_key", "api_key").build();
    }

    //    Метод возвращает RequestSpecification для метода POST baseULR/store/order
    protected RequestSpecification placeOrderForPet(String body) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpecification())
                .setBasePath("store/order")
                .setBody(body).build();
    }

    protected RequestSpecification findOrderByID(String orderId) {
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpecification())
                .setBasePath("store/order/" + orderId)
                .build();
    }

    protected RequestSpecification returnsPetInventories(){
        return new RequestSpecBuilder()
                .addRequestSpecification(getBaseSpecification())
                .setBasePath("store/inventory")
                .build();
    }

    //________________________________________________________________________________________________________________
    //________________________________________________________________________________________________________________


    //    Методы возвращают спецификацию проверок для методов ручки
    protected ResponseSpecification getAssertionSpec(int statusCode) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(statusCode);
        return builder.build();
    }

    protected ResponseSpecification placeOrderForPetResp(int petId, String status, int quantity) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpec(200))
                .expectBody("petId", equalTo(petId))
                .expectBody("status", equalTo(status))
                .expectBody("quantity", equalTo(quantity));
        return builder.build();
    }

    protected ResponseSpecification orderForPetResp(int code, String type, String message) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpec(code))
                .expectBody("code", equalTo(code))
                .expectBody("type", equalTo(type))
                .expectBody("message", equalTo(message));
        return builder.build();
    }

    protected ResponseSpecification returnsPetInventoriesResp() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.addResponseSpecification(getAssertionSpec(200));
                return builder.build();
    }
}


