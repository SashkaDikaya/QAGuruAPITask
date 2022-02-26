import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.zip.ZipFile;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasKey;

public class HomeWorkTests {

    @Test
    void listUsersTest() {

        given()
                .contentType(JSON)
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body("total", is(12))
                .body("total_pages", is(2))
                .body("data.last_name", hasItems("Lawson", "Ferguson"))
                .body("data.last_name", hasKey("email"));
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
