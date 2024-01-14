import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class RegistrationTests {

    @Test
    void successfulRegistrationTest () {
        String registrationData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
                .body(registrationData)
                .contentType(JSON)
                .log().uri().

        when()
                .post("https://reqres.in/api/register").

        then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", greaterThan(0))
                .body("token", notNullValue());
    }

    @Test
    void unsuccessfulRegistrationWithEpmpryEmailTest () {
        String registrationData = "{\"email\": \"\", \"password\": \"pistol\"}";

        given()
                .body(registrationData)
                .contentType(JSON)
                .log().uri().

        when()
                .post("https://reqres.in/api/register").

        then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void unsuccessfulRegistrationWithEmptyPasswordTest () {
        String registrationData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"\"}";

        given()
                .body(registrationData)
                .contentType(JSON)
                .log().uri().

        when()
                .post("https://reqres.in/api/register").

        then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void unsuccessfulRegistrationWithEmptyBody () {
        String registrationData = "{}";

        given()
                .body(registrationData)
                .contentType(JSON)
                .log().uri().

        when()
                .post("https://reqres.in/api/register").

        then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    void unsuccessfulRegistrationWithoutContentTypeHeaderTest () {
        String registrationData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        given()
                .body(registrationData)
                .log().uri().

        when()
                .post("https://reqres.in/api/register").

        then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }
}
