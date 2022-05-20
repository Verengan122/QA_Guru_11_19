package spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static listner.CustomAllureListener.withCustomTemplates;

public class Specs {
    public static RequestSpecification request = with()
            .filter(withCustomTemplates())
            .baseUri("https://reqres.in")
            .log().uri()
            .log().body()
            .log().cookies()
            .contentType(ContentType.JSON);

    public static RequestSpecification requestDemoWebShop = with()
            .filter(withCustomTemplates())
            .baseUri("http://demowebshop.tricentis.com")
            .log().uri()
            .log().body()
            .log().params()
            .contentType("application/x-www-form-urlencoded; charset=UTF-8");

    public static ResponseSpecification positiveSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();

    public static ResponseSpecification negativeSpec = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .build();

    public static ResponseSpecification notFoundSpec = new ResponseSpecBuilder()
            .expectStatusCode(404)
            .build();
}
