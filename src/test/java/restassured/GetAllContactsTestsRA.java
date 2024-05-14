package restassured;

import com.jayway.restassured.RestAssured;
import dto.ContactDTO;
import dto.ErrorDTO;
import dto.GetAllContactsDTO;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;

public class GetAllContactsTestsRA {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoia3Jhc2xlb0BnbWFpbC5jb20iLCJpc3MiOiJSZWd1bGFpdCIsImV4cCI6MTcxNjMwNTE1NiwiaWF0IjoxNzE1NzA1MTU2fQ.m92DW-Vlk0e5lZ4oXVTYhYfLBHYBTaTOVczTDO4VL8A";
    @BeforeMethod
    public void preCondition(){
        RestAssured.baseURI = "https://contactapp-telran-backend.herokuapp.com";
        RestAssured.basePath = "v1";
    }

    @Test
    public void getAllContactsSuccess(){
        GetAllContactsDTO contactsDTO = given()
                .header("Authorization", token)
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(200)
                .extract()
                .response()
                .as(GetAllContactsDTO.class);
        List<ContactDTO> list = contactsDTO.getContacts();// to get the list of the contacts
        for (ContactDTO contact:list) { //iteration of list -- on each iteration 1 contact will be received
            System.out.println(contact.getId());
            System.out.println(contact.getEmail());
            System.out.println("Size of list---->" + list.size());
        }
    }

    @Test
    public void getAllContactsWrongToken(){
        ErrorDTO errorDTO = given()
                .header("Authorization","tuyutut")
                .when()
                .get("contacts")
                .then()
                .assertThat().statusCode(401)
                .extract()
                .response()
                .as(ErrorDTO.class);
        System.out.println(errorDTO.getError());
        Assert.assertEquals(errorDTO.getError(), "Unauthorized");
    }
}
