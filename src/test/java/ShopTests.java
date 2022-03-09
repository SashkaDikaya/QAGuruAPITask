import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.closeWindow;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;


public class ShopTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
    }

    @BeforeEach
    void beforeEach () {
        open(baseURI);
    }

    @AfterEach
    void afterEch() {
        closeWindow();
    }

    @Test
    void addItemIntoCardTest() {

        Integer cardSize = 1;

        given()
                .log().all()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=5dde7bcb-dbd6-4064-b0e7-18739a4fed42")
                .body("product_attribute_28_7_10=28&product_attribute_28_1_11=31&addtocart_28.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/28/1")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .body("updatetopcartsectionhtml", is("(" + cardSize + ")"));
    }

    @Test
    void addItemIntoWishListTest() {

        Integer wishListSize = 1;

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=21045fcb-7782-4d01-81a1-fda4f9b86b51")
                .body("product_attribute_28_7_10=26&product_attribute_28_1_11=30&addtocart_28.EnteredQuantity=1")
                .when()
                .post("/addproducttocart/details/28/2")
                .then()
                .log().all()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/wishlist\">wishlist</a>"))
                .body("updatetopwishlistsectionhtml", is("(" + wishListSize + ")"));
    }

    @Test
    void loginTest() {

        String authorizationCookie =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("Email=alex_wild%40mail.ru&Password=123456&RememberMe=false")
                        .when()
                        .post("/login")
                        .then()
                        .log().all()
                        .statusCode(302)
                        .extract()
                        .cookie("NOPCOMMERCE.AUTH");

        open(baseURI);

        getWebDriver().manage().addCookie(
                new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));

        open(baseURI + "/customer/info");

        $x("//label[@for='Email']//following-sibling::input").shouldHave(value("alex_wild@mail.ru"));
    }

    @Test
    void searchRingTest() {

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .cookie("Nop.customer=efa04ead-b036-4630-86b1-5bd8875d03c6")
                .when()
                .get("/search?q=ring")
                .then()
                .log().all()
                .statusCode(200);

        open(baseURI + "/search?q=ring");

        $x("//div[@class='product-grid']").shouldHave(text("Diamond Engagement Ring"));

    }


}