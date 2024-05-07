package restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import dto.AuthRequestDTO;
import dto.AuthResponseDTO;
import dto.ErrorDTO;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.SQLOutput;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class LoginTestsRA {
    String endpoint = "user/login/usernamepassword";
    @BeforeMethod
    public void preCondition(){// to have base url every time
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        //base path - a part that common for all the end points
        RestAssured.basePath = "v1";
    }

    @Test
    public void loginSuccess(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("krasleo@gmail.com")
                .password("Cristiano7777$!")
                .build();

        AuthResponseDTO responseDTO = given().//to give parameters of request
                body(auth)
                .contentType("application/json")
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(AuthResponseDTO.class);
        System.out.println(responseDTO.getToken());
    }

    @Test
    public void loginWrongEmail(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("krasleogmail.com")
                .password("Cristiano7777$!")
                .build();

        ErrorDTO errorDTO = given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response()
                .as(ErrorDTO.class);
        Assert.assertEquals(errorDTO.getMessage(),"Login or Password incorrect");
    }


    @Test
    public void loginWrongEmailFormat(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("krasleogmail.com")
                .password("Cristiano7777$!")
                .build();

      given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message",containsString("Login or Password incorrect"))
              .assertThat().body("path",equalTo("/v1/user/login/usernamepassword"));
    }

    @Test
    public void loginWrongPassword() {
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("krasleo@gmail.com")
                .password("Cristiano7777")
                .build();

        ErrorDTO errorDTO = given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response()
                .as(ErrorDTO.class);
        Assert.assertEquals(errorDTO.getMessage(), "Login or Password incorrect");
    }

    @Test
    public void loginWrongPasswordFormat(){
        AuthRequestDTO auth = AuthRequestDTO.builder()
                .username("krasleo@gmail.com")
                .password("Cristiano7777")
                .build();

        given()
                .body(auth)
                .contentType(ContentType.JSON)
                .when()
                .post(endpoint)
                .then()
                .assertThat().statusCode(401)
                .assertThat().body("message",containsString("Login or Password incorrect"))
                .assertThat().body("path",equalTo("/v1/user/login/usernamepassword"));
    }

}
