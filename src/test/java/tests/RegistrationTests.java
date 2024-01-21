package tests;

import io.restassured.RestAssured;
import models.RegisterBodyModel;
import models.RegisterErrorModel;
import models.RegisterResponseModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.junit.jupiter.api.Assertions.*;
import static specs.RegisterSpec.*;

public class RegistrationTests {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
    }

    RegisterBodyModel regData = new RegisterBodyModel();

    @Test
    @DisplayName("Отправка успешного запроса на регистрацию нового пользователя")
    void successfulRegistrationTest () {

        regData.setEmail("eve.holt@reqres.in");
        regData.setPassword("pistol");


       RegisterResponseModel response = step("Send request", () ->
               given(registerRequestSpec)
                   .body(regData)

               .when()
                   .post()

               .then()
                   .spec(registerResponseSpec)
                   .extract().as(RegisterResponseModel.class));

       step("Check response", () -> {
               assertNotEquals("0", response.getId());
               assertNotNull(response.getToken());
       });

    }

    @Test
    @DisplayName("Отправка неуспешного запроса на регистрацию нового пользователя с пустой почтой")
    void registrationWithEmptyEmailTest () {

        regData.setEmail("");
        regData.setPassword("pistol");

        RegisterErrorModel response = step("Send request", () ->
        given(registerRequestSpec)
                .body(regData).

        when()
                .post().

        then()
                .spec(errorResponseSpec)
                .extract().as(RegisterErrorModel.class));

        step("Check response", () ->
                assertEquals("Missing email or username", response.getError()));
    }

    @Test
    @DisplayName("Отправка неуспешного запроса на регистрацию нового пользователя с пустым паролем")
    void registrationWithEmptyPasswordTest () {

        regData.setEmail("eve.holt@reqres.in");
        regData.setPassword("");

        RegisterErrorModel response = step("Send request", () ->
        given(registerRequestSpec)
                .body(regData).

        when()
                .post().

        then()
                .spec(errorResponseSpec)
                .extract().as(RegisterErrorModel.class));
        step("Check response", () ->
                assertEquals("Missing password", response.getError()));
    }

    @Test
    @DisplayName("Отправка неуспешного запроса на регистрацию нового пользователя с пустым телом")
    void registrationWithEmptyBody () {

        RegisterErrorModel response = step("Send request", () ->
        given(registerRequestSpec)
                .body("").

        when()
                .post().

        then()
                .spec(errorResponseSpec)
                .extract().as(RegisterErrorModel.class));
        step("check response", () ->
                assertEquals("Missing email or username", response.getError()));
    }

    @Test
    @DisplayName("Отправка неуспешного запроса на регистрацию нового пользователя без заголовка")
    void registrationWithoutContentTypeHeaderTest () {

        regData.setEmail("eve.holt@reqres.in");
        regData.setPassword("pistol");

        RegisterErrorModel response = step("Send request", () ->
        given(registerRequestSpecWithoutHeader)
                .body(regData).

        when()
                .post().

        then()
                .spec(errorResponseSpec)
                .extract().as(RegisterErrorModel.class));
        step("Check response", () ->
                assertEquals("Missing email or username", response.getError()));
    }
}
