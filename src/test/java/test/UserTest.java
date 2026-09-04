package test;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserTest extends BaseTest {

    // 5 endpoıntdır  tam crud

    //get /user

    @Test (groups = {"smoke", "user"},
    description = "Istıfadecı sıyahısı tokensız oxunur(public api")
    public void siyahiPublicOxunur(){
        Response response =
          given()
                .spec(publicSpec())
          .when()
                .get("/users");

        response.then()
                .statusCode(200)
                .contentType(containsString("json"))
                .time(lessThan(MAX_TIME_MS));

        List<Map<String, Object>> users = response.jsonPath().getList("$");
        Assert.assertFalse(users.isEmpty(), "Istifadeçi siyahısı bosdur");

        System.out.println("Gelen user sayı : " + users.size());
    }

    @Test(groups = {"regression","users"},
    description = "İstifadeci id ile oxunur ve email formatı duzgundur")

    public void istifadeciIdİleOxunur(){

        String id = ilkUserId();

        given()
                .spec(publicSpec())
                .pathParams("id",id)
        .when()
                .get("/users/{id}")
        .then()
                .statusCode(200)
                .contentType(containsString("json"))
                .body("email",containsString("@test.com"))
                .body("phone",containsString("+994"))
                .body("password",nullValue());
    }

    //Komekcı
    private  String ilkUserId(){
        Response user =
            given()
                 .spec(publicSpec())
            .when()
                 .get("/users");
        user.then().statusCode(200);

        String id = user.jsonPath().getString("[0].id");

        Assert.assertNotNull(id,"İstifadeci id si tapılmadı ");
        return id;
    }
}
