package tests;

import models.lombok.GenerateDataLombok;
import models.lombok.UserData;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static listner.CustomAllureListener.withCustomTemplates;
import static org.assertj.core.api.Assertions.assertThat;
import static spec.Specs.*;

public class TestsAddAllure {
    @DisplayName("Успешный тест на наличие пользователя")
    @Test
    void succsessfulTest() {
        UserData userData = new UserData();
        userData.setEmail("eve.holt@reqres.in");
        userData.setPassword("cityslicka");
        GenerateDataLombok dataLombok =
                given()
                        .spec(request)
                        .body(userData)
                        .when()
                        .post("/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .spec(positiveSpec)
                        .extract().as(GenerateDataLombok.class);

        assertThat(dataLombok.getToken()).isNotNull();

    }

    @DisplayName("Неуспешный тест на наличие пользователя")
    @Test
    void unsuccsessfulTest() {
        UserData userData = new UserData();
        userData.setEmail("sydney@fife");
        GenerateDataLombok dataLombok =
                given()
                        .spec(request)
                        .body(userData)
                        .when()
                        .post("/api/login")
                        .then()
                        .log().status()
                        .log().body()
                        .spec(negativeSpec)
                        .extract().as(GenerateDataLombok.class);

        assertThat(dataLombok.getError()).isEqualTo("Missing password");

    }

    @DisplayName("Создание пользователя")
    @Test
    void createTest() {
        UserData userData = new UserData();
        userData.setName("morpheus");
        userData.setJob("leader");
        given()
                .spec(request)
                .body(userData)
                .when()
                .post("/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201);

        assertThat(userData.getName()).isEqualTo("morpheus");
        assertThat(userData.getJob()).isEqualTo("leader");

    }

    @DisplayName("Наличие в Data 12 элементов")
    @Test
    void listTest() {
        GenerateDataLombok dataLombok =
                given()
                        .spec(request)
                        .get("/api/users?page=2")
                        .then()
                        .log().status()
                        .log().body()
                        .spec(positiveSpec)
                        .extract().as(GenerateDataLombok.class);

        assertThat(dataLombok.getTotal()).isEqualTo("12");

    }

    @DisplayName("Ошибка 404")
    @Test
    void notFoundTest() {
        given()
                .spec(request)
                .get("/api/unknown/23")
                .then()
                .log().status()
                .log().body()
                .spec(notFoundSpec);

    }

    @DisplayName("Попытка подписки с неверным Email")
    @Test
    void subscriptionWithInvalidMail() {
        UserData userData = new UserData();
        userData.setEmail("RedCat12");
        given()
                .filter(withCustomTemplates())
                .formParam("email", userData.getEmail())
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .log().uri()
                .log().body()
                .log().params()
                .when()
                .post("http://demowebshop.tricentis.com/subscribenewsletter")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("Result", Matchers.is("Enter valid email"))
                .body("Success", Matchers.is(false));

    }

    @DisplayName("Подписка с верным Email")
    @Test
    void subscriptionWithTrueMail() {
        UserData userData = new UserData();
        userData.setEmail("RedCat12@mail.ru");
        given()
                .filter(withCustomTemplates())
                .formParam("email", userData.getEmail())
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .log().uri()
                .log().body()
                .log().params()
                .when()
                .post("http://demowebshop.tricentis.com/subscribenewsletter")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("Result", Matchers.is("Thank you for signing up!" +
                        " A verification email has been sent. We appreciate your interest."))
                .body("Success", Matchers.is(true));


    }
}

