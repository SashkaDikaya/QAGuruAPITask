import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class HomeWorkTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
        RestAssured.filters(withCustomTemplates());
    }


    @Test
    void listUsersTest() {

        given()
                .contentType(JSON)
                .log().uri()
                .log().body()
                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.last_name", hasItems("Lawson", "Ferguson"))
                .body("data.first_name", hasItems("Byron", "George", "Rachel"))
                .body("data.avatar", hasItem("https://reqres.in/img/faces/11-image.jpg"))
                .body("$", hasKey("page"))
                .body("data", everyItem(hasKey("email")));
    }

    @Test
    void listUsersTestWithAssertJ() {
        Response response = get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        int total = response.path("total");
        int page = response.path("page");

        assertEquals(12, total);
        assertEquals(2, page);

    }

    @Test
    void listResourseTestWithAssertJ() {
        Response response = get("/unknown")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        assertThat(response).isNotNull();

    }

    @Test
    void createUserTest() {

        JSONObject requestBody = new JSONObject()
                .put("name", "morpheus")
                .put("job", "leader");

        given()
                .contentType(JSON)
                .log().uri()
                .log().body()
                .body(requestBody.toString())
                .when()
                .post("/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void registerUserTest() {

        JSONObject requestBody = new JSONObject()
                .put("email", "eve.holt@reqres.in")
                .put("password", "pistol");

        given()
                .contentType(JSON)
                .log().uri()
                .log().body()
                .body(requestBody.toString())
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void updateUserTest() {

        JSONObject requestBody = new JSONObject()
                .put("name", "morpheus")
                .put("job", "zion resident");

        given()
                .contentType(JSON)
                .log().uri()
                .log().body()
                .body(requestBody.toString())
                .when()
                .post("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("job", is("zion resident"));
    }

    @Test
    void userNotFoundTest() {

        given()
                .contentType(JSON)
                .get("/unknown/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);

    }

    @Test
    void successfulLogin() {

        JSONObject requestBody = new JSONObject()
                .put("email", "eve.holt@reqres.in")
                .put("password", "cityslicka");

        given()
                .contentType(JSON)
                .log().uri()
                .log().body()
                .body(requestBody.toString())
                .when()
                .post("/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }


}
