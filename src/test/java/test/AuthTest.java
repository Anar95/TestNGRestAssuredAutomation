package test;

import base.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthTest extends BaseTest {

    // AuthTest - login----401----403 senarileri

    @Test(groups = {"smoke","auth"},description = "Düzgün melumat ile uğurlu login")

    public void uğurluLogin(){

        Map<String,String> body = new HashMap<>();
        body.put("email",ADMIN_EMAIL);
        body.put("password",ADMIN_PASSWORD);

         given()
                .spec(publicSpec())
                .body(body)
         .when()
                .post("/auth/login")
         .then()
                 .statusCode(200)   //1 Status codu
                 .body("token",notNullValue())  // 2 token geldı?
                 .body("token", not(emptyString()))  //3 token fieldi  bos deyıl
                 .time(lessThan(MAX_TIME_MS));    // 4 respons süreti
    }

    @Test (groups = {"regression","auth","securty"}
    ,description = "Tehlukseizlik: login cavabında şifre qaytarılmamalıdır")

    public void cavabdsŞifreYoxdur(){
        Map<String,String> body = new HashMap<>();
        body.put("email",ADMIN_EMAIL);
        body.put("password",ADMIN_PASSWORD);

        Response response =
              given()
                .spec(publicSpec())
                .body(body)
             .when()
                .post("/auth/login");

        response.then()
                .statusCode(200)
                .body("password", nullValue()) //kok sevıyyesınde password fıeldı yoxdur yoxlayır
                .body("user.password",nullValue());// user obyectındede yoxdur


        // Xamm metnde de axtarırıq- pwd ve ya passwordHash

        Assert.assertFalse(response.asString().toLowerCase().contains("password"),
                "Cavabda parolla bağlı sahe var! :" + response.asString()
        );
    }
        // Neqtıve senari

        @Test (groups = {"regression", "auth", "neqative"},description = "Sehv şifre ile login testi")
                public void sehvSifreLogin(){

            Map<String,String> body = new HashMap<>();
            body.put("email",ADMIN_EMAIL);
            body.put("password","TestDemoYalnısPassword");

            given()
                    .spec(publicSpec())
                    .body(body)
            .when()
                    .post("/auth/login")
            .then()
                    .statusCode(401)
                    .body("token",(nullValue())); // hec bır halda token verılmememlıdır
        }

        @Test (groups = {"neqativ","auth"},description = "Uydurma token ıle request")
          public void uydurmaToken() {
            given()
                    .spec(authSpec("uydurma.token.1234567"))
                    .when()
                    .get("/accounts")
                    .then()
                    .statusCode(401);

        }

        // Bir metod 4 senari

            @DataProvider(name="natamamLogin")
                    public Object[][] natamamLogin(){

                Map<String,String> yalnizEmail = new HashMap<>();
                yalnizEmail.put("email",ADMIN_EMAIL);

                Map<String,String> yalnizPassword = new HashMap<>();
                yalnizPassword.put("password",ADMIN_PASSWORD);

                Map<String,String> bosSaheler = new HashMap<>();
                bosSaheler.put("email","");
                bosSaheler.put("password","");

                return new Object[][]{
                        {"yalnız Email", yalnizEmail},
                        {"yalnız Password", yalnizPassword},
                        {"bos Saheler", bosSaheler},
                        {"tamamile boş body", new HashMap<String,String>()}


            };
        }
            @Test (groups = {"regression","negative"},
            dataProvider = "natamamLogin",
            description = "Natamam login sorguları redd edilir")
            public void natamamLoginReddEdilir (String ssenari, Map<String,String> body){

                given()
                        .spec(publicSpec())
                        .body(body)

                        .when()
                        .post("/auth/login")

                        .then()
                        .statusCode(anyOf(is(400),is(401),is(422)));
                // 3 variantdan biri meqbuldu. Amma 200 hec bırınde yoxdu

            }

        }


