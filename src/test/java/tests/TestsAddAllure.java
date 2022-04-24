package tests;

import com.fasterxml.jackson.databind.DeserializationFeature;
import io.restassured.RestAssured;
import models.lombok.GenerateDataLombok;
import models.lombok.UserData;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static listner.CustomAllureListener.withCustomTemplates;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class TestsAddAllure {
    @Test
    void succsessfulTest() {
        UserData userData = new UserData();
        userData.setEmail("eve.holt@reqres.in");
        userData.setPassword("cityslicka");
        GenerateDataLombok dataLombok =
        given()
                .filter(withCustomTemplates())
                .body(userData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(GenerateDataLombok.class);

       assertThat(dataLombok.getToken()).isNotNull();

    }

    @Test
    void unsuccsessfulTest() {
        UserData userData = new UserData();
        userData.setEmail("sydney@fife");
        GenerateDataLombok dataLombok =
        given()
                .filter(withCustomTemplates())
                .body(userData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .extract().as(GenerateDataLombok.class);

        assertThat(dataLombok.getError()).isEqualTo("Missing password");

    }

    @Test
    void createTest() {
        UserData userData = new UserData();
        userData.setName("morpheus");
        userData.setJob("leader");
        given()
                .filter(withCustomTemplates())
                .body(userData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201);

        assertThat(userData.getName()).isEqualTo("morpheus");
        assertThat(userData.getJob()).isEqualTo("leader");

    }

    @Test
    void listTest() {
GenerateDataLombok dataLombok = new GenerateDataLombok();

        RestAssured.get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200);
//                .body("total", is(12))


        assertThat(dataLombok.getTotal()).hasSize(12);
    }

    @Test
    void notFoundTest() {
        RestAssured.get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404);

    }

    @Test
    void SubscriptionWithInvalidMail() {
        UserData userData = new UserData();
        userData.setEmail("RedCat12");
        GenerateDataLombok dataLombok =
        given()
                .body(userData)
//                .formParam("email", "RedCat12")
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .log().uri()
                .log().body()
                .when()
                .post("http://demowebshop.tricentis.com/subscribenewsletter")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
//                .body("Result", Matchers.is("Enter valid email"))
//                .body("Success", Matchers.is(false))
                .extract().as(GenerateDataLombok.class);


        assertThat(dataLombok.getResult()).isEqualTo("Enter valid email");
//        assertThat(dataLombok.getSuccess()).isEqualTo(false);

    }

    @Test
    void SubscriptionWithTrueMail() {
        given()
                .formParam("email", "RedCat12@mail.ru")
                .when()
                .post("http://demowebshop.tricentis.com/subscribenewsletter")
                .then()
                .log().all()
                .statusCode(200)
                .body("Result", Matchers.is("Thank you for signing up! A verification email has been sent. We appreciate your interest."))
                .body("Success", Matchers.is(true));
    }
}

