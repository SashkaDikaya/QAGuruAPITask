import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeWorkTests {

    @Test
    void listUsersTest() {

        given()
                .contentType(JSON)
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("data.last_name", hasItems("Lawson", "Ferguson"))
                .body("data.first_name", hasItems("Byron", "George", "Rachel"))
                .body("data.avatar", hasItem("https://reqres.in/img/faces/11-image.jpg"))
                .body("$", hasKey("page"))
                .body("data", everyItem(hasKey("email")));
    }

    @Test
    void listUsersTestWithAssertJ() {
        Response response = get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .extract().response();

        int total = response.path("total");
        int page = response.path("page");

        assertEquals(12, total);
        assertEquals(2, page);

    }

    @Test
    void listResourseTestWithAssertJ() {
        Response response = get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .extract().response();

        assertThat(response).isNotNull();

    }

    @Test
    void createUserTest() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void registerUserTest() {

        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void updateUserTest() {

        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users/2")
                .then()
                .statusCode(201)
                .body("job", is("zion resident"));
    }

    @Test
    void userNotFoundTest() {

        given()
                .contentType(JSON)
                .get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404);

    }

    @Test
    void successfulLogin() {

        String data = "{ \"email\": \"eve.holt@reqres.in\"," +
                " \"password\": \"cityslicka\" }";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }


}
